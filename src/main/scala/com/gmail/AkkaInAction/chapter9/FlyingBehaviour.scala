package com.gmail.AkkaInAction.chapter9

import akka.actor.{FSM, Actor, ActorRef}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.ControlSurfaces.{StickLeft, StickRight, StickBack, StickForward}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.Plane.GiveMeControl
import com.gmail.AkkaInAction.chapter6.Altimeter.AltitudeUpdate
import com.gmail.AkkaInAction.chapter6.EventSource.{RegisterListener, UnregisterListener}
import com.gmail.AkkaInAction.chapter7.Pilots.{Controls, RelinquishControl}
import com.gmail.AkkaInAction.chapter9.FlyingBehaviour._
import com.gmail.AkkaInAction.chapter9.HeadingIndicator.HeadingUpdate
import scala.concurrent.duration._

/**
 * Created by rayanral on 14/06/15.
 */
object FlyingBehaviour {

  sealed trait State
  case object Idle extends State
  case object Flying extends State
  case object PreparingToFly extends State


  case class CourseTarget(altitude: Double,
                          heading: Float,
                          byMillis: Long)

  case class CourseStatus(altitude: Double,
                          heading: Float,
                          headingSinceMs: Long,
                          altitudeSinceMs: Long)

  type Calculator = (CourseTarget, CourseStatus) => Any

  sealed trait Data
  case object Uninitialized extends Data
  case class FlightData(controls: ActorRef,
                        elevCalc: Calculator,
                        bankCalc: Calculator,
                        target: CourseTarget,
                        status: CourseStatus) extends Data

  case class Fly(target: CourseTarget)

  def currentMS = System.currentTimeMillis

  def calcElevator(target: CourseTarget, status: CourseStatus) = {
    val alt = (target.altitude - status.altitude).toFloat
    val duration = target.byMillis - status.altitudeSinceMs
    if(alt < 0)
      StickForward((alt / duration) * -1)
    else
      StickBack(alt / duration)
  }

  def calcAilerons(target: CourseTarget, status: CourseStatus): Any = {
    import scala.math.{abs, signum}

    val diff = target.heading - status.heading
    val dur = target.byMillis - status.headingSinceMs
    val amount =
      if (abs(diff) < 180)
        diff
      else
        signum(diff) * (abs(diff) - 360f)

    if (amount > 0)
      StickRight(amount / dur)
    else
      StickLeft((amount / dur) * -1)
  }


}


class FlyingBehaviour(plane: ActorRef,
                      heading: ActorRef,
                      altimeter: ActorRef) extends Actor with FSM[FlyingBehaviour.State, FlyingBehaviour.Data] {

  case object Adjust

  startWith(Idle, Uninitialized)

  def adjust(flightData: FlightData): FlightData = {
    val FlightData(c, elevCalc, bankCalc, t, s) = flightData
    c ! elevCalc(t, s)
    c ! bankCalc(t, s)
    flightData
  }

  when(Idle) {
    case Event(Fly(target), _) =>
      goto(PreparingToFly) using FlightData(context.system.deadLetters,
                                            calcElevator,
                                            calcAilerons,
                                            target,
                                            CourseStatus(-1, -1, 0, 0))
  }

  onTransition {
    case Idle -> PreparingToFly =>
      plane ! GiveMeControl
      heading ! RegisterListener(self)
      altimeter ! RegisterListener(self)
  }

  def prepComplete(data: Data) = {
    data match {
      case FlightData(c, _, _, _, s) =>
        if (c != context.system.deadLetters && s.heading != -1f && s.altitude != -1f)
          true
        else
          false
      case _ =>
        false
    }
  }


  onTransition {
    case PreparingToFly -> Flying => setTimer("Adjustment", Adjust, 200.milliseconds, repeat = true)
  }

  when(PreparingToFly)(transform {
    case Event(HeadingUpdate(head), d: FlightData) =>
      stay using d.copy(status = d.status.copy(heading = head, headingSinceMs = currentMS))
    case Event(AltitudeUpdate(alt), d: FlightData) =>
      stay using d.copy(status = d.status.copy(altitude = alt,
        altitudeSinceMs = currentMS))
    case Event(Controls(ctrls), d: FlightData) =>
      stay using d.copy(controls = ctrls)
    case Event(StateTimeout, _) =>
      //      plane ! LostControl
      goto(Idle)
  } using {
    case s if prepComplete(s.stateData) =>
      s.copy(stateName = Flying)
  })

  onTransition {
    case Flying -> _ => cancelTimer("Adjustment")
  }


  when(Flying) {
    case Event(Adjust, flightData: FlightData) =>
      stay using adjust(flightData)
    case Event(AltitudeUpdate(alt), d: FlightData) =>
      stay using d.copy(status = d.status.copy(altitude = alt,
        altitudeSinceMs = currentMS))
    case Event(HeadingUpdate(head), d: FlightData) =>
      stay using d.copy(status = d.status.copy(heading = head))
  }


  onTransition {
    case _ -> Idle =>
      heading ! UnregisterListener(self)
      altimeter ! UnregisterListener(self)
  }

  whenUnhandled {
    case Event(RelinquishControl, _) => goto(Idle)
  }

  initialize
}


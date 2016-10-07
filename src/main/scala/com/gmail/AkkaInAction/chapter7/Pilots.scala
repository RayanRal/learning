package com.gmail.AkkaInAction.chapter7

import akka.actor.FSM.{CurrentState, Transition, SubscribeTransitionCallBack}
import akka.actor.{Terminated, ActorRef, Actor}
import akka.stream.actor.ActorPublisherMessage.Request
import akka.util.Timeout
import com.gmail.AkkaInAction.chapter5.ex5Clouds.ControlSurfaces.{StickBack, StickForward}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.Plane.GiveMeControl
import com.gmail.AkkaInAction.chapter5.ex5Clouds.{ControlSurfaces, Plane}
import com.gmail.AkkaInAction.chapter7.AutoPilot.{CoPilotReference, RequestCoPilot}
import com.gmail.AkkaInAction.chapter7.Pilots.ReadyToGo
import com.gmail.AkkaInAction.chapter9
import com.gmail.AkkaInAction.chapter9.DrinkingBehaviour.{FeelingSober, FeelingTipsy, FeelingLikeZaphod}
import com.gmail.AkkaInAction.chapter9.{FlyingProvider, DrinkingProvider, DrinkingBehaviour, FlyingBehaviour}
import com.gmail.AkkaInAction.chapter9.FlyingBehaviour.{Idle, Fly, CourseTarget, Calculator}
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by rayanral on 01/06/15.
 */
object Pilots {

  case object ReadyToGo
  case object RelinquishControl
  case class Controls(controlSurfaces: ActorRef)

}

object Pilot {

  import FlyingBehaviour._
  import ControlSurfaces._

  val tipsyCalcElevator: Calculator  = { (target, status) =>
    val msg = calcElevator(target, status)
    msg match {
      case StickForward(amt) => StickForward(amt * 1.03f)
      case StickBack(amt) => StickBack(amt * 1.03f)
      case m => m
    }
  }

  // Calculates the aileron changes when we're a bit tipsy
  val tipsyCalcAilerons: Calculator = { (target, status) =>
    val msg = calcAilerons(target, status)
    msg match {
      case StickLeft(amt) => StickLeft(amt * 1.03f)
      case StickRight(amt) => StickRight(amt * 1.03f)
      case m => m
    }
  }

  // Calculates the elevator changes when we're totally out of it
  val zaphodCalcElevator: Calculator = { (target, status) =>
    val msg = calcElevator(target, status)
    msg match {
      case StickForward(amt) => StickBack(1f)
      case StickBack(amt) => StickForward(1f)
      case m => m
    }
  }

  // Calculates the aileron changes when we're totally out of it
  val zaphodCalcAilerons: Calculator = { (target, status) =>
    val msg = calcAilerons(target, status)
    msg match {
      case StickLeft(amt) => StickRight(1f)
      case StickRight(amt) => StickLeft(1f)
      case m => m
    }
  }

}

class Pilot(plane: ActorRef,
             autopilot: ActorRef,
             var controls: ActorRef,
             altimeter: ActorRef) extends Actor {
  this: DrinkingProvider with FlyingProvider =>

  import Pilots._
  import Plane._

  var copilot = context.system.deadLetters

  val copilotName = context.system.settings.config.getString("zzz.akka.avionics.flightcrew.copilotName")

  override def preStart(): Unit = {
    context.actorOf(newDrinkingBehaviour(self))
    context.actorOf(newFlyingBehaviour(plane, controls, altimeter))
  }

  def bootstrap: Receive = {
    case ReadyToGo =>
      val copilot = Await.result(context.actorSelection("../" + copilotName).resolveOne(1 second), 1 second)
      val flyer = Await.result(context.actorSelection("FlyingBehaviour").resolveOne(1 second), 1 second)
      flyer ! SubscribeTransitionCallBack(self)
      flyer ! Fly(CourseTarget(20000, 250, System.currentTimeMillis + 30000))
      context.become(sober(copilot, flyer))
  }

  def sober(copilot: ActorRef, flyer: ActorRef): Receive = {
    case FeelingSober =>
    // We're already sober
    case FeelingTipsy =>
      becomeTipsy(copilot, flyer)
    case FeelingLikeZaphod =>
      becomeZaphod(copilot, flyer)
  }

  def tipsy(copilot: ActorRef, flyer: ActorRef): Receive = {
    case FeelingSober =>
      becomeSober(copilot, flyer)
    case FeelingTipsy =>
    // We're already tipsy
    case FeelingLikeZaphod =>
      becomeZaphod(copilot, flyer)
  }

  // The 'zaphod' behaviour
  def zaphod(copilot: ActorRef, flyer: ActorRef): Receive = {
    case FeelingSober =>
      becomeSober(copilot, flyer)
    case FeelingTipsy =>
      becomeTipsy(copilot, flyer)
    case FeelingLikeZaphod =>
    // We're already Zaphod
  }

  // The 'idle' state is merely the state where the Pilot does nothing at all
  def idle: Receive = {
    case _ =>
  }

  def becomeSober(copilot: ActorRef, flyer: ActorRef) = {
//    flyer ! NewElevatorCalculator(calcElevator)
//    flyer ! NewBankCalculator(calcAilerons)
    context.become(sober(copilot, flyer))
  }

  def becomeTipsy(copilot: ActorRef, flyer: ActorRef) = {
//    flyer ! NewElevatorCalculator(tipsyCalcElevator)
//    flyer ! NewBankCalculator(tipsyCalcAilerons)
    context.become(tipsy(copilot, flyer))
  }

  def becomeZaphod(copilot: ActorRef, flyer: ActorRef) = {
//    flyer ! NewElevatorCalculator(zaphodCalcElevator)
//    flyer ! NewBankCalculator(zaphodCalcAilerons)
    context.become(zaphod(copilot, flyer))
  }


  override def unhandled(msg: Any): Unit = {
    msg match {
      case Transition(_, _, Idle) =>
        context.become(idle)
      // Ignore these two messages from the FSM rather than have them
      // go to the log
      case Transition(_, _, _) =>
      case CurrentState(_, _) =>
      case m => super.unhandled(m)
    }
  }

  def receive = bootstrap

}


class CoPilot(plane: ActorRef,
              var controls: ActorRef,
              altimeter: ActorRef) extends Actor {

  var autopilot = context.system.deadLetters
  val pilotName = context.system.settings.config.getString("zzz.akka.avionics.flightcrew.pilotName")
  var pilot = context.system.deadLetters
  implicit val timeout: Timeout = 1.second


  def receive = {
    case ReadyToGo =>
      pilot = Await.result(context.actorSelection("../" + pilotName).resolveOne(), 1.second)
      context.watch(pilot)
    case Terminated(_) =>
      plane ! GiveMeControl
  }


}

object AutoPilot {
  case class RequestCoPilot()

  case class CoPilotReference(coPilot: ActorRef)

}

class AutoPilot(plane: ActorRef) extends Actor {

  def receive = {
    case ReadyToGo =>
      plane ! RequestCoPilot
    case CoPilotReference(coPilotRef) =>
      context.watch(coPilotRef)
    case Terminated(_) =>
      plane ! GiveMeControl
  }

}
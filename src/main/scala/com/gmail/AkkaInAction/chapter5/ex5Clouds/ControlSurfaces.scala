package com.gmail.AkkaInAction.chapter5.ex5Clouds

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.Altimeter.RateChange
import com.gmail.AkkaInAction.chapter5.ex5Clouds.ControlSurfaces.{StickForward, StickBack}
import com.gmail.AkkaInAction.chapter9.HeadingIndicator

/**
 * Created by rayanral on 17/05/15.
 */
object ControlSurfaces {

  case class StickBack(amount: Float)
  case class StickForward(amount: Float)

  case class StickLeft(amount: Float)
  case class StickRight(amount: Float)

  case class HasControl(somePilot: ActorRef)

}

class ControlSurfaces(plane: ActorRef,
                      altimeter: ActorRef,
                      heading: ActorRef) extends Actor {

  import ControlSurfaces._
  import Altimeter._
  import HeadingIndicator._

  def receive = controlledBy(context.system.deadLetters)

  def controlledBy(somePilot: ActorRef): Receive = {
    case StickBack(amount) if sender == somePilot =>
      altimeter ! RateChange(amount)
    case StickForward(amount) if sender == somePilot =>
      altimeter ! RateChange(amount * -1)
    case StickRight(amount) =>
      heading ! BankChange(amount)
    case StickLeft(amount) =>
      heading ! BankChange(amount * -1)

    case HasControl(entity) if sender == plane =>
      context.become(controlledBy(entity))
  }

}
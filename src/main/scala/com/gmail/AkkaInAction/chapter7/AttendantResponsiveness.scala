package com.gmail.AkkaInAction.chapter7

import akka.actor.{Cancellable, ActorRef, Actor}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by rayanral on 31/05/15.
 */
trait AttendantResponsiveness {

  val maxResponseTimeMS: Int

  def responseDuration = scala.util.Random.nextInt(maxResponseTimeMS).millis

}

object FlightAttendant {
  case class GetDrink(drinkname: String)

  case class Drink(drinkname: String)

  case class Assist(passenger: ActorRef)

  case object Busy_?
  case object Yes
  case object No

  def apply() = new FlightAttendant with AttendantResponsiveness {
    val maxResponseTimeMS = 30000
  }

}

class FlightAttendant extends Actor { this: AttendantResponsiveness =>
  import com.gmail.AkkaInAction.chapter7.FlightAttendant._

  case class DeliveryDrink(drink: Drink)

  var pendingDelivery: Option[Cancellable] = None

  def scheduleDelivery(drinkname: String): Cancellable = {
    context.system.scheduler.scheduleOnce(responseDuration, self, DeliveryDrink(Drink(drinkname)))
  }


  //cancel all deliveries and save injured with potion
  def assistInjuredPassenger: Receive = {
    case Assist(passenger) =>
      pendingDelivery.foreach { _.cancel() }
      pendingDelivery = None
      passenger ! Drink("magic healing potion")
  }

  def handleDrinkingRequests: Receive = {
    case GetDrink(drinkname) =>
      pendingDelivery = Some(scheduleDelivery(drinkname))
      context.become(assistInjuredPassenger orElse handleSpecificPerson(sender()))
    case Busy_? =>
      sender ! No
  }

  def handleSpecificPerson(passenger: ActorRef): Receive = {
    case GetDrink(drinkname) if sender == passenger =>
      pendingDelivery.foreach { _.cancel() }
      pendingDelivery = Some(scheduleDelivery(drinkname))
    case DeliveryDrink(drink) =>
      passenger ! drink
      pendingDelivery = None
      context.become(assistInjuredPassenger orElse handleDrinkingRequests)
    case m: GetDrink =>
      context.parent forward(m)
    case Busy_? =>
      sender ! Yes
  }

  def receive = assistInjuredPassenger orElse handleDrinkingRequests
}
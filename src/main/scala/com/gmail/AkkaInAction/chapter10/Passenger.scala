package com.gmail.AkkaInAction.chapter10

import akka.actor.{ActorLogging, Actor, ActorRef}

/**
 * Created by rayanral on 17/06/15.
 */
object Passenger {

  case object FastenSeatbelts
  case object UnfastenSeatbelts

  val seatAssignment = """([\w\s_]+)-(\d+)-([A-Z])""".r

}

trait DrinkingRequestProbability {

  import scala.concurrent.duration._

  val askTreshold = 0.9f

  val requestMin = 20.minutes

  val requestUpper = 30.minutes

  def randomishTime(): FiniteDuration = {
    requestMin + scala.util.Random.nextInt(requestUpper.toMillis.toInt).millis
  }

}


trait PassengerProvider {

  def newPassenger(callButton: ActorRef): Actor = new Passenger(callButton) with DrinkingRequestProbability
}

class Passenger(callButton: ActorRef) extends Actor with ActorLogging {
  this: DrinkingRequestProbability =>

  import Passenger._
  import com.gmail.AkkaInAction.chapter7.FlightAttendant._
  import scala.collection.JavaConverters._
  import scala.concurrent.ExecutionContext.Implicits.global

  val rand = scala.util.Random
  val scheduler = context.system.scheduler

  case object CallForDrink

  val seatAssignment(myName, _, _) = self.path.name.replaceAll("_", " ")
  val drinks = context.system.settings.config.getStringList("zzz.akka.avionics.drinks").asScala.toIndexedSeq

  override def preStart(): Unit = {
    self ! CallForDrink
  }

  def maybeSendDrinkRequest(): Unit = {
    if(rand.nextFloat() > askTreshold) {
      val drinkname = drinks(rand.nextInt(drinks.length))
      callButton ! GetDrink(drinkname)
    }
    scheduler.scheduleOnce(randomishTime(), self, CallForDrink)
  }

  def receive = {
    case CallForDrink =>
      maybeSendDrinkRequest()
    case Drink(drinkname) =>
      log.info(f"$myName received a $drinkname - Yum!")
    case FastenSeatbelts =>
      log.info(f"$myName fastening seatbelts")
    case UnfastenSeatbelts =>
      log.info(f"$myName UNfastening seatbelts")
  }

}
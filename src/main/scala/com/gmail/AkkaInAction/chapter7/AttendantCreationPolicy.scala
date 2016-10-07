package com.gmail.AkkaInAction.chapter7

import akka.actor.{Props, ActorRef, Actor}
import com.gmail.AkkaInAction.chapter7.LeadFlightAttendant.{Attendant, GetFlightAttendant}

/**
 * Created by rayanral on 31/05/15.
 */
trait AttendantCreationPolicy {

  val numberOfAttendants: Int = 8

  def createAttendant: Actor = FlightAttendant()

}


trait LeadFlightAttendantProvider {

  def newFlightAttendant: Actor = LeadFlightAttendant()

}


object LeadFlightAttendant {
  case object GetFlightAttendant
  case class Attendant(a: ActorRef)

  def apply() = new LeadFlightAttendant with AttendantCreationPolicy
}

class LeadFlightAttendant extends Actor{ this: AttendantCreationPolicy =>

  override def preStart() = {
    import scala.collection.JavaConverters._
    val attendantNames = context.system.settings.config.getStringList("zzz.akka.avionics.flightcrew.attendantNames").asScala
    attendantNames.take(numberOfAttendants).foreach { i =>
      context.actorOf(Props(createAttendant), i)
    }
  }

  def receive = {
    case GetFlightAttendant =>
      sender ! Attendant(randomAttendant())
    case m =>
      randomAttendant().forward(m)
  }

  def randomAttendant(): ActorRef = context.children.take(scala.util.Random.nextInt(numberOfAttendants) + 1).last
}
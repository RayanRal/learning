package com.gmail.AkkaInAction.chapter14

import akka.actor.{Props, Actor, ActorRef}
import com.gmail.AkkaInAction.chapter14.Airport.{Ignore, HeadTo}
import com.gmail.AkkaInAction.chapter14.Beacon.BeaconHeading
import com.gmail.AkkaInAction.chapter9.FlyingBehaviour
import com.gmail.AkkaInAction.chapter9.FlyingBehaviour.{Fly, CourseTarget}

import scala.concurrent.duration._

/**
 * Created by rayanral on 28/06/15.
 */
trait AirportSpecifics {
  lazy val headingTo: Float = 0.0f
  lazy val altitude: Double = 0
}

object Airport {

  case class HeadTo(flyingBehaviour: ActorRef)
  case class Ignore(flyingBehaviour: ActorRef)

  def torontoAirport(): Actor = new Airport with BeaconProvider with AirportSpecifics {
    override lazy val headingTo = 314.3f
    override lazy val altitude: Double = 26000.0
  }
}

class Airport extends Actor {
  this: BeaconProvider with AirportSpecifics =>

  val beacon = context.actorOf(Props(newBeacon(headingTo)), "Beacon")

  def receive = {
    case HeadTo(flyingBehaviour) =>
      val when = (1 hour fromNow).time.toMillis

      context.actorOf(Props(new MessageTransformer(from = beacon,
                                                   to = flyingBehaviour, {
        case BeaconHeading(heading) =>
          Fly(CourseTarget(altitude, heading, when))
      })))

    case Ignore(_) =>
      context.children.foreach { context.stop }
  }

}

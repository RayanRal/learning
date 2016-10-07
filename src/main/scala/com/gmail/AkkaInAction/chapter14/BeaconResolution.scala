package com.gmail.AkkaInAction.chapter14

/**
 * Created by rayanral on 28/06/15.
 */

import akka.actor.{Actor, ActorRef}
import akka.event.{EventStream, ActorEventBus}
import com.gmail.AkkaInAction.chapter14.Beacon.BeaconHeading
import com.gmail.AkkaInAction.chapter14.GenericPublisher.{UnregisterListener, RegisterListener}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

trait BeaconResolution {
  lazy val beaconInterval = 1.second
}

trait BeaconProvider {
  def newBeacon(heading: Float) = Beacon(heading)
}

object GenericPublisher {
  case class RegisterListener(actor: ActorRef)
  case class UnregisterListener(actor: ActorRef)
}


object Beacon {

  case class BeaconHeading(heading: Float)

  def apply(heading: Float) = new Beacon(heading) with BeaconResolution

}

class Beacon(heading: Float) extends Actor {
  this: BeaconResolution =>

  case object Tick

  val bus = new EventStream(context.system)

  val ticker = context.system.scheduler.schedule(beaconInterval, beaconInterval, self, Tick)


  def receive = {
    case RegisterListener(actor) =>
      bus.subscribe(actor, BeaconHeading.getClass)
    case UnregisterListener(actor) =>
      bus.unsubscribe(actor)
    case Tick =>
      bus.publish(BeaconHeading(heading))
  }


}

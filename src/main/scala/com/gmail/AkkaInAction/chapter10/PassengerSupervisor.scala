package com.gmail.AkkaInAction.chapter10

import akka.actor.SupervisorStrategy.{Stop, Resume, Escalate}
import akka.actor._
import akka.routing.BroadcastGroup
import com.typesafe.config.ConfigList

import scala.concurrent.Await

/**
 * Created by rayanral on 17/06/15.
 */
object PassengerSupervisor {

  case object GetPassengerBroadcaster

  case class PassengerBroadcaster(broadcaster: ActorRef)

  def apply(callButton: ActorRef, bathrooms: ActorRef) = new PassengerSupervisor(callButton, bathrooms) with PassengerProvider

}


class PassengerSupervisor(callButton: ActorRef, bathrooms: ActorRef) extends Actor {
  this: PassengerProvider =>

  import PassengerSupervisor._
  import scala.concurrent.duration._

  override val supervisorStrategy = OneForOneStrategy() {
    case _: ActorKilledException => Escalate
    case _: ActorInitializationException => Escalate
    case _ => Resume
  }

  case class GetChildren(forSomeone: ActorRef)

  case class Children(children: scala.collection.immutable.Iterable[ActorRef], childrenFor: ActorRef)

  override def preStart(): Unit = {
    context.actorOf(Props(new Actor {
      val config = context.system.settings.config
      override val supervisorStrategy = OneForOneStrategy() {
        case _: ActorKilledException => Escalate
        case _: ActorInitializationException => Escalate
        case _ => Stop
      }

      override def preStart(): Unit = {
        val passengers = config.getList("zzz.akka.avionics.passengers")
//        passengers.forEach { nameWithSeat =>
//          val id = nameWithSeat.asInstanceOf[ConfigList].unwrapped().mkString("-")
//          context.actorOf(Props(newPassenger(callButton)), id)
//        }
      }

      def receive = {
        case GetChildren(forSomeone: ActorRef) =>
          sender ! Children(context.children, forSomeone)
      }
    }))
  }

  def receive = noRouter

  def noRouter: Receive = {
    case GetPassengerBroadcaster =>
      Await.result(context.actorSelection("PassengerSupervisor").resolveOne(1 second), 1 second) ! GetChildren(sender)
    case Children(passengers, destinedFor) =>
      val router = context.actorOf(Props().withRouter(BroadcastGroup(passengers.map(ac => ac.path.address.toString))), "Passengers")
      destinedFor ! PassengerBroadcaster(router)
      context.become(withRouter(router))
  }

  def withRouter(router: ActorRef): Receive = {
    case GetPassengerBroadcaster =>
      sender ! PassengerBroadcaster(router)
  }

}
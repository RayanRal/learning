package com.gmail.AkkaInAction.chapter8

import akka.actor.SupervisorStrategy.{Restart, Escalate, Stop}
import akka.actor._
import com.gmail.AkkaInAction.chapter7.Pilot

/**
 * Created by rayanral on 07/06/15.
 */

case class Initialize()


class MyActor extends Actor {


  override def preStart(): Unit = {
    self ! Initialize
    context.watch(context.actorOf(Props[Pilot]))
  }

  override def postStop() = {
    //CLEANUP
  }

  def receive = {
    case Initialize =>
    case Terminated(deadActor) =>
      println(s"Actor has died ${deadActor.path.name}")
  }

  override def supervisorStrategy = OneForOneStrategy() {
    case _: ActorInitializationException => Stop
    case _: ActorKilledException         => Stop
    case _: Exception                    => Restart
    case _                               => Escalate
  }

}

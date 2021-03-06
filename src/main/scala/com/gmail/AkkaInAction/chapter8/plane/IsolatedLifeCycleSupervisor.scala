package com.gmail.AkkaInAction.chapter8.plane

import akka.actor.Actor
import com.gmail.AkkaInAction.chapter8.plane.IsolatedLifeCycleSupervisor.{Started, WaitForStart}

/**
 * Created by rayanral on 08/06/15.
 */
object IsolatedLifeCycleSupervisor {

  case object WaitForStart

  case object Started

}


trait IsolatedLifeCycleSupervisor extends Actor {

  def receive = {
    case WaitForStart =>
      sender ! Started
    case m =>
      throw new Exception(s"Don't call ${self.path.name} directly ($m)")
  }

  def childStarter(): Unit

  //only start the children when we started
  final override def preStart() = {
    childStarter()
  }

  //don't call preStart
  final override def postRestart(reason: Throwable) = { }

  //don't stop the children
  final override def preRestart(reason: Throwable, message: Option[Any]) = { }


}
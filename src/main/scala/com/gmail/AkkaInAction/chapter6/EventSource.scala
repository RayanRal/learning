package com.gmail.AkkaInAction.chapter6

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}
import com.gmail.AkkaInAction.chapter6.EventSource.{RegisterListener, UnregisterListener}

/**
 * Created by rayanral on 17/05/15.
 */
object EventSource {

  case class RegisterListener(listener: ActorRef)
  case class UnregisterListener(listener: ActorRef)

}

trait EventSource {

  def sendEvent[T](event: T): Unit
  def eventSourceReceive: Receive

}

trait ProductionEventSource extends EventSource {this: Actor =>

  var listeners = Vector.empty[ActorRef]

  // Sends the event to all of our listeners
  def sendEvent[T](event: T): Unit = listeners foreach {_ ! event}

  // We create a specific partial function to handle the messages for
  // our event listener.  Anything that mixes in our trait will need to
  // compose this receiver
  def eventSourceReceive: Receive = {
    case RegisterListener(listener) =>
      listeners = listeners :+ listener
    case UnregisterListener(listener) =>
      listeners = listeners filter { _ != listener }
  }

}
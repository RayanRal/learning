package com.gmail.AkkaInAction.chapter17.pat1

import akka.actor.Actor

import scala.collection.mutable

/**
 * Created by rayanral on 01/07/15.
 */
trait ReceiveCompositingActor extends Actor {

  type Priority = Int
  lazy val receivePartials = mutable.Map.empty[Priority, Receive]

  val startOfReceiveChain: Priority = 0
  val endOfReceiveChain: Priority = 10000

  def becomeNew(key: Int, behaviour: Receive): Unit = {
    receivePartials += (key -> behaviour)
    context.become(composeReceive)
  }

  def composeReceive: Receive = {
    receivePartials.toSeq.sortBy {
      case (key, _) => key
    }.map {
      case (_, value) => value
    }.reduceLeft { (a, b) => a orElse b }
  }

  override def preStart(): Unit = {
    super.preStart()
    context.become(composeReceive)
  }


  //pointless, we're becoming on start
  final def receive: Receive = { case _ => }

}

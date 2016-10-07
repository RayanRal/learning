package com.gmail.AkkaInAction.chapter8

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
 * Created by rayanral on 08/06/15.
 */
class MyOtherActor extends Actor {

  def initialize(): Unit = {
    //some initialization
  }

  override def preStart(): Unit = {
    initialize()
    //start children here
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    //default was to stop children bu we don't want to do that
    postStop()
  }

  override def postRestart(reason: Throwable): Unit = {
    //we're not calling preStart, to not get children started
    initialize()
  }

  override def receive: Receive = ???
}

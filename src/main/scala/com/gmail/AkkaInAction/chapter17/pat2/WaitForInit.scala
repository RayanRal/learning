package com.gmail.AkkaInAction.chapter17.pat2

import akka.actor.Actor

/**
 * Created by rayanral on 01/07/15.
 */
class WaitForInit extends Actor {

  def uninitialized: Receive = {
    case DBResults(results) =>
      context.become(initialized(results))
    case HTTPRequest(request) =>
      sender ! "System not ready"
  }

  def initialized(data: Map[String, String]): Receive = {
    case HTTPRequest(request) =>
      sender ! "Answer"
  }

  def receive = uninitialized

}

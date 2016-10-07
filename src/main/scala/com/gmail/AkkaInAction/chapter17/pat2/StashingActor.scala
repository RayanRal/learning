package com.gmail.AkkaInAction.chapter17.pat2

import akka.actor.{Actor, Stash}

/**
 * Created by rayanral on 01/07/15.
 */
//should be used with UnboundedDequeBasedMailbox
class StashingActor extends Actor with Stash {

  def uninitialized: Receive = {
    case DBResults(results) =>
      unstashAll()
      context.become(initialized(results))
    case HTTPRequest(_) =>
      stash()
  }

  def initialized(data: Map[String, String]): Receive = {
    case HTTPRequest(req) =>
      sender ! "Answer"
  }

  def receive = uninitialized

}

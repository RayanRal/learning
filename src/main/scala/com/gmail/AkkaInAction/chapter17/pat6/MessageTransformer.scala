package com.gmail.AkkaInAction.chapter17.pat6

import akka.actor.{Actor, ActorRef}

/**
 * Created by rayanral on 02/07/15.
 */
class MessageTransformer(from: ActorRef, to: ActorRef, transformer: PartialFunction[Any, Any]) extends Actor {

  def receive = {
    case m => to forward transformer(m)
  }

}

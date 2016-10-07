package com.gmail.AkkaInAction.chapter14

import akka.actor.{Actor, ActorRef}

/**
 * Created by rayanral on 28/06/15.
 */
class MessageTransformer(from: ActorRef, to: ActorRef, transformer: PartialFunction[Any, Any]) extends Actor {
  import GenericPublisher._

  override def preStart(): Unit = {
    from ! RegisterListener(self)
  }

  override def postStop(): Unit = {
    from ! UnregisterListener(self)
  }


  def receive = {
    case m => to.forward(transformer(m))
  }

}

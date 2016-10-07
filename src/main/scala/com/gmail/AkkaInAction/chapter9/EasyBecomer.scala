package com.gmail.AkkaInAction.chapter9

import akka.actor.Actor
import com.gmail.AkkaInAction.chapter9.EasyBecomer.{Goodbye, Hello}

/**
 * Created by rayanral on 13/06/15.
 */

object EasyBecomer {
  case class Hello(greeting: String)

  case object Goodbye
}

class EasyBecomer extends Actor {

  def expectHello: Receive = {
    case Hello(greeting) =>
      sender ! Hello(greeting + " to you too!")
      context.become(expectGoodbye)
    case Goodbye =>
      sender ! "Huh? Who are you?"
  }

  def expectGoodbye: Receive = {
    case Hello(_) =>
      sender ! "We've already done that"
    case Goodbye =>
      sender ! "So long, dude!"
      context.become(expectHello)
  }

  def receive = expectHello

}

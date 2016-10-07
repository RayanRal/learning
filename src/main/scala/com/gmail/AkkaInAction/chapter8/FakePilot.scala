package com.gmail.AkkaInAction.chapter8

import akka.actor.{Props, ActorSystem, Actor}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}
import scala.concurrent.duration.Duration

/**
 * Created by rayanral on 10/06/15.
 */
class FakePilot extends Actor {

  def receive = {
    case _ => throw new Exception("Expected exception")
  }

}

class NilActor extends Actor {
  def receive = {
    case _ =>
  }
}

object PilotSpec {

  val copilotName = "Mary"
  val pilotName = "Mark"

}

class PilotSpec extends TestKit(ActorSystem("PilotSpec")) with ImplicitSender with WordSpecLike with MustMatchers {

  def nilActor = system.actorOf(Props[NilActor])



}
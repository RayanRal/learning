package com.gmail.AkkaInAction.chapter6

import akka.actor.{Actor, Props, ActorRef, ActorSystem}
import akka.testkit.TestKit
import org.scalatest._

/**
 * Created by rayanral on 23/05/15.
 */
class MyActorSpec extends TestKit(ActorSystem("MyActorSpec"))
                            with WordSpecLike
                            with MustMatchers
                            with BeforeAndAfterAll
                            with BeforeAndAfterEach {

  override def afterAll() { system.terminate() }

  override def afterEach() {}

  def makeActor(): ActorRef = system.actorOf(Props[MyActor], "MyActor")

  "My Actor" should {
    an [Exception] must be thrownBy  {
        val a = system.actorOf(Props[MyActor])
    }

    "construct without exception" in {
      val a = makeActor()
    }

    "respond with a Pong to a Ping" in {
      val a = makeActor()
      a ! Ping
      expectMsg(Pong)
    }
  }

}

case class Ping()
case class Pong()

class MyActor extends Actor {
  override def receive: Receive = {
    case Ping =>
      sender ! Pong
  }
}
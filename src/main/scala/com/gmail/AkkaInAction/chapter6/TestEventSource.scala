package com.gmail.AkkaInAction.chapter6

import akka.actor.{ActorSystem, Actor}
import akka.actor.Actor.Receive

import akka.testkit._
import com.gmail.AkkaInAction.chapter6.EventSource.{UnregisterListener, RegisterListener}
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, MustMatchers, WordSpec}

/**
 * Created by rayanral on 18/05/15.
 */
class TestEventSource extends Actor with ProductionEventSource {
  override def receive: Receive = eventSourceReceive
}

class EventSourceSpec extends TestKit(ActorSystem("EventSourceSpec"))
                              with WordSpecLike
                              with MustMatchers
                              with BeforeAndAfterAll {

  override def afterAll() { system.terminate() }

  "EventSource" should {
    "allow us to register a listener" in {
      val real = TestActorRef[TestEventSource].underlyingActor
      real.receive(RegisterListener(testActor))
      real.listeners must contain (testActor)
    }

    "allow us to unregister a listener" in {
      val testActor = TestActorRef[TestEventSource]
      testActor ! RegisterListener(testActor)
      testActor.underlyingActor.listeners.size mustBe 1
      testActor ! UnregisterListener(testActor)
      testActor.underlyingActor.listeners.size mustBe 0
    }

    "send the event to our test actor" in {
      val testA = TestActorRef[TestEventSource]
      testA ! RegisterListener(testActor)
      testA.underlyingActor.sendEvent("Fibonacci")
      expectMsg("Fibonacci")
    }
  }

}

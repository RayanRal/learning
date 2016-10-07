package com.gmail.AkkaInAction.chapter10

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorSystem, Actor, ActorRef}
import akka.testkit.{ImplicitSender, TestKit}
import com.gmail.AkkaInAction.chapter10.PassengerSupervisor.{PassengerBroadcaster, GetPassengerBroadcaster}
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, BeforeAndAfterAll, WordSpecLike, WordSpec}

/**
 * Created by rayanral on 19/06/15.
 */
object PassengerSupervisorSpec {

  val config = ConfigFactory.parseString("""zzz.akka.avionics.passengers = [
                                           |      ["Kelly Franqui", "01", "A"],
                                           |      ["Tyrone Dotts", "02", "B"],
                                           |      ["Malinda Class", "03", "C"],
                                           |      ["Kenya Jolicoeur", "04", "A"],
                                           |      ["Christian Piche", "05", "B"],
                                           |      ["Neva Delapena", "06", "C"],
                                         """)

}


trait TestPassengerProvider extends PassengerProvider {
  override def newPassenger(callButton: ActorRef): Actor =
    new Actor {
      override def receive: Receive = {
        case m => callButton ! m
      }
    }
}

class PassengerSupervisorSpec extends TestKit(ActorSystem("PassengerSupervisorSpec", PassengerSupervisorSpec.config))
                              with ImplicitSender
                              with WordSpecLike
                              with BeforeAndAfterAll
                              with MustMatchers  {

  import scala.concurrent.duration._

  override def afterAll(): Unit = {
    system.terminate()
  }

  "PassengerSupervisor" should {
    "work" in {
      val a = system.actorOf(Props(new PassengerSupervisor(testActor, testActor) with TestPassengerProvider))

      a ! GetPassengerBroadcaster
      val broadcaster = expectMsgPF() {
        case PassengerBroadcaster(b) =>
          b ! "Hithere"

          expectMsg("Hithere")
          expectMsg("Hithere")
          expectMsg("Hithere")
          expectMsg("Hithere")
          expectMsg("Hithere")

          expectNoMsg(100.milliseconds)

          b
      }

      a ! GetPassengerBroadcaster
      expectMsg(PassengerBroadcaster(broadcaster))
    }
  }

}

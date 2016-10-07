package com.gmail.AkkaInAction.chapter7

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import com.gmail.AkkaInAction.chapter7.FlightAttendant.GetDrink
import com.typesafe.config.ConfigFactory
import org.scalatest.{WordSpecLike, MustMatchers, WordSpec}

/**
 * Created by rayanral on 31/05/15.
 */
object TestFlightAttendant {

  def apply() = new FlightAttendant with AttendantResponsiveness {
    val maxResponseTimeMS = 1
  }

}


class FlightAttendantSpec extends TestKit(ActorSystem("FlightAttendantSpec",
                                          ConfigFactory.parseString("akka.scheduler.tick-duration = 1ms")))
                                    with ImplicitSender
                                    with WordSpecLike
                                    with MustMatchers {

  import FlightAttendant._

  "FlightAttendant" should {
    "get a drink when asked" in {
      val a = TestActorRef(Props(TestFlightAttendant()))
      a ! GetDrink("soda")
      expectMsg(Drink("soda"))
    }
  }

}

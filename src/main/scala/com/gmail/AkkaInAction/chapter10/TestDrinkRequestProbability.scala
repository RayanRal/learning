package com.gmail.AkkaInAction.chapter10

import akka.actor.{Props, ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.gmail.AkkaInAction.chapter10.Passenger.FastenSeatbelts
import org.scalatest.{MustMatchers, WordSpecLike, Matchers}
import org.scalatest.matchers.ShouldMatchers

/**
 * Created by rayanral on 17/06/15.
 */
trait TestDrinkRequestProbability extends DrinkingRequestProbability {

  import scala.concurrent.duration._

  override val askTreshold = 0f
  override val requestMin = 0 milliseconds
  override val requestUpper = 2 milliseconds

}

//class PassengerSpec extends TestKit(ActorSystem()) with ImplicitSender with Matchers with WordSpecLike with MustMatchers {
//
//  import akka.event.Logging.Info
//  import akka.testkit.TestProbe
//
//  var seatNumber = 5
//
//  def newPassenger(): ActorRef = {
//    seatNumber += 1
//    system.actorOf(Props(new Passenger(testActor) with TestDrinkRequestProbability), s"Pat_Metheny-$seatNumber-B")
//  }
//
//  "Passengers" should {
//    "fasten seatbelts when asked" in {
//      val a = newPassenger()
//      val p = TestProbe()
//      system.eventStream.subscribe(p.ref, classOf[Info])
//      a ! FastenSeatbelts
//      p.expectMsgPF() {
//        case Info(_, _, m) =>
//          m.toString must include ("fastening seatbelt")
//      }
//    }
//  }
//
//}

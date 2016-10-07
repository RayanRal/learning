package com.gmail.AkkaInAction.chapter7

import akka.actor.Props

/**
 * Created by rayanral on 31/05/15.
 */
object FlightAttendantPathChecker {

  def main(args: Array[String]): Unit = {
    val system = akka.actor.ActorSystem("PlaneSimulation")
    val lead = system.actorOf(Props(new LeadFlightAttendant with AttendantCreationPolicy), "LeadFlightAttendant")
    Thread.sleep(2000)
    system.terminate()
  }

}

package com.gmail.AkkaInAction.chapter8.plane

import akka.actor.{ActorRef, Actor}
import com.gmail.AkkaInAction.chapter7.{FlightAttendant, AutoPilot, CoPilot, Pilot}
import com.gmail.AkkaInAction.chapter9.{FlyingProvider, DrinkingProvider}

/**
 * Created by rayanral on 10/06/15.
 */
trait PilotProvider {

  def newPilot(plane: ActorRef, autopilot: ActorRef, controls: ActorRef, altimeter: ActorRef): Actor = new Pilot(plane, autopilot, controls, altimeter) with DrinkingProvider with FlyingProvider
  def newCoPilot(plane: ActorRef, autopilot: ActorRef, altimeter: ActorRef): Actor = new CoPilot(plane, autopilot, altimeter)
  def newFlightAttendant: Actor = FlightAttendant()
  def newAutoPilot(plane: ActorRef): Actor = new AutoPilot(plane)

}

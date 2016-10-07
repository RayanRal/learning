package com.gmail.AkkaInAction.chapter5.ex5Clouds

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{OneForOneStrategy, Props, Actor, ActorLogging}
import akka.agent.Agent
import akka.routing.{RoundRobinPool, RoundRobinRoutingLogic, FromConfig}
import akka.testkit.TestActor.AutoPilot
import akka.util.Timeout
import com.gmail.AkkaInAction.chapter10.PassengerSupervisor
import com.gmail.AkkaInAction.chapter15.{Bathroom, Female, Male, GenderAndTime}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.Altimeter.AltitudeUpdate
import com.gmail.AkkaInAction.chapter5.ex5Clouds.Plane.GiveMeControl
import com.gmail.AkkaInAction.chapter7.AutoPilot.RequestCoPilot
import com.gmail.AkkaInAction.chapter7.{Pilots, LeadFlightAttendant, CoPilot, Pilot}
import com.gmail.AkkaInAction.chapter7.Pilots.Controls
import com.gmail.AkkaInAction.chapter8.plane.IsolatedLifeCycleSupervisor.WaitForStart
import com.gmail.AkkaInAction.chapter8.plane.{PilotProvider, OneForOneStrategyFactory, IsolatedStopSupervisor}

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 17/05/15.
 */
object Plane {

  // Returns the control surface to the Actor that asks for them
  case object GiveMeControl

}


// We want the Plane to own the Altimeter and we're going to do that
// by passing in a specific factory we can use to build the Altimeter
class Plane extends Actor with ActorLogging with PilotProvider {

  val config = context.system.settings.config
  implicit val timeout: Timeout = 1.second

  val maleBathroomCounter = Agent(GenderAndTime(Male, 0.seconds, 0))
  val femaleBathroomCounter = Agent(GenderAndTime(Female, 0.seconds, 0))

    def receive: Receive = {
      case GiveMeControl =>
        log.info("Giving control")
//        sender ! Controls(controls)
      case AltitudeUpdate(altitude) =>
        log.info(s"Altitude is now: $altitude")
      case RequestCoPilot =>
//
    }
  //  context.actorOf(Props[AutoPilot], "AutoPilot")
  //  val flightAttendant = context.actorOf(Props(LeadFlightAttendant()), config.getString("zzz.akka.avionics.flightcrew.leadAttendantName"))

    override def preStart(): Unit = {
      startUtilities()
      startPeople()
    }


  def actorForControls(name: String) = Await.result(context.actorSelection("Controls/" + name).resolveOne(), 1.second)


  def startUtilities(): Unit = {
    context.actorOf(Props(new Bathroom(femaleBathroomCounter, maleBathroomCounter))
      .withRouter(RoundRobinPool(nrOfInstances = 4, supervisorStrategy = OneForOneStrategy() {
      case _ => Resume
    })), "Bathrooms")
  }

  def startPeople(): Unit = {
    val plane = self
    val controls = actorForControls("ControlSurfaces")
    val altimeter = actorForControls("Altimeter")
    val autopilot = actorForControls("AutoPilot")
    val leadFlightAttendant = context.actorOf(Props(newFlightAttendant).withRouter(FromConfig()), "LeadFlightAttendant")
    val bathrooms = actorForControls("Bathrooms")

    val people = context.actorOf(Props(new IsolatedStopSupervisor with OneForOneStrategyFactory{
      override def childStarter(): Unit = {
        context.actorOf(Props(PassengerSupervisor(leadFlightAttendant, bathrooms)), "Passengers")
        context.actorOf(Props(newCoPilot(plane, autopilot, altimeter)), config.getString("zzz.akka.avionics.flightcrew.coPilotName"))
        context.actorOf(Props(newPilot(plane, autopilot, controls, altimeter)), config.getString("zzz.akka.avionics.flightcrew.coPilotName"))
        context.actorOf(Props(newAutoPilot(plane)), "autopilot")
      }
    }), "Pilots")

    Await.result(people ? WaitForStart, 1.second)
  }
}

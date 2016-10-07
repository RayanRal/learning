package com.gmail.AkkaInAction.chapter5.ex5Clouds

import akka.actor.{ActorRef, Props, ActorSystem}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.ControlSurfaces.{StickForward, StickBack}
import com.gmail.AkkaInAction.chapter5.ex5Clouds.Plane.GiveMeControl
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by rayanral on 17/05/15.
 */
object Avionics {

  implicit val timeout = Timeout(5.seconds)
  val as = ActorSystem("PlaneSimulation")
  val plane = as.actorOf(Props[Plane], "Plane")

  def main(args: Array[String]): Unit = {
    val control = Await.result((plane ? GiveMeControl).mapTo[ActorRef], 5.seconds)
    as.scheduler.scheduleOnce(200.millis) {
      control ! StickBack(1f)
    }
    as.scheduler.scheduleOnce(1.second) {
      control ! StickBack(0f)
    }
    as.scheduler.scheduleOnce(3.seconds) {
      control ! StickBack(0.5f)
    }
    as.scheduler.scheduleOnce(4.seconds) {
      control ! StickBack(0f)
    }
    as.scheduler.scheduleOnce(5.seconds) {
      as.terminate()
    }
  }

}

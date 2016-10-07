package com.gmail.AkkaInAction.chapter9

import akka.actor.{Props, Actor, ActorRef}
import com.gmail.AkkaInAction.chapter9.DrinkingBehaviour.{FeelingLikeZaphod, FeelingTipsy, FeelingSober, LevelChanged}

import scala.concurrent.duration._

/**
 * Created by rayanral on 16/06/15.
 */
object DrinkingBehaviour {

  case class LevelChanged(level: Float)

  case object FeelingSober
  case object FeelingTipsy
  case object FeelingLikeZaphod

  def apply(drinker: ActorRef) = new DrinkingBehaviour(drinker) with DrinkingResolution
}

class DrinkingBehaviour(drinker: ActorRef) extends Actor {
  this: DrinkingResolution =>
  import scala.concurrent.ExecutionContext.Implicits.global

  var currentLevel = 0f

  val scheduler = context.system.scheduler

  val sobering = scheduler.schedule(initialSobering, soberingInterval, self, LevelChanged(-0.0001f))

  override def postStop: Unit = {
    sobering.cancel()
  }

  override def preStart: Unit = {
    drink
  }

  def drink = scheduler.scheduleOnce(drinkInterval(), self, LevelChanged(0.005f))

  def receive = {
    case LevelChanged(amount) =>
      currentLevel = currentLevel + amount

      if(currentLevel <= 0.01) {
        drink
        drinker ! FeelingSober
      } else if (currentLevel <= 0.03) {
        drink
        drinker ! FeelingTipsy
      } else
        drinker ! FeelingLikeZaphod

  }

}

trait DrinkingResolution {

  import scala.util.Random


  def initialSobering: FiniteDuration = 1.second
  def soberingInterval: FiniteDuration = 1.second
  def drinkInterval(): FiniteDuration = Random.nextInt(300).seconds

}

trait DrinkingProvider {
  def newDrinkingBehaviour(drinker: ActorRef): Props = Props(DrinkingBehaviour(drinker))
}

trait FlyingProvider {

  def newFlyingBehaviour(plane: ActorRef,
                         heading: ActorRef,
                         altimeter: ActorRef): Props =
    Props(new FlyingBehaviour(plane, heading, altimeter))

}
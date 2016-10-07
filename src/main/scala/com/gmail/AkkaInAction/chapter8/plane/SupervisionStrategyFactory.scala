package com.gmail.AkkaInAction.chapter8.plane

import akka.actor.{AllForOneStrategy, OneForOneStrategy, SupervisorStrategy}
import akka.actor.SupervisorStrategy.Decider

import scala.concurrent.duration.Duration

/**
 * Created by rayanral on 08/06/15.
 */
trait SupervisionStrategyFactory {

  def makeStrategy(maxRetries: Int, withinTime: Duration)(decider: Decider): SupervisorStrategy

}

trait OneForOneStrategyFactory extends SupervisionStrategyFactory {

  def makeStrategy(maxRetries: Int, withinTime: Duration)(decider: Decider): SupervisorStrategy =
      OneForOneStrategy(maxRetries, withinTime)(decider)

}

trait AllForOneStrategyFactory extends SupervisionStrategyFactory {

  def makeStrategy(maxRetries: Int, withinTime: Duration)(decider: Decider): SupervisorStrategy =
    AllForOneStrategy(maxRetries, withinTime)(decider)

}

package com.gmail.AkkaInAction.chapter10

import akka.actor.{SupervisorStrategy, Props, ActorSystem, Actor}
import akka.routing.{RoundRobinPool, RoundRobinRoutingLogic, FromConfig}

/**
 * Created by rayanral on 17/06/15.
 */
class DBConnection extends Actor {

  def receive = {
    case _ =>
  }

}

object DBAccessor extends App {

  val system = ActorSystem("system")
  val dbRouter = system.actorOf(Props[DBConnection]
                       .withRouter(FromConfig(routerDispatcher = "DatabaseConnectionRouter")), "DBRouter")

  val dbRouterRound = system.actorOf(
    Props.empty
      .withRouter(RoundRobinPool(nrOfInstances = 5,
                                 supervisorStrategy = SupervisorStrategy.defaultStrategy)
                 ), "DBRouter")

}
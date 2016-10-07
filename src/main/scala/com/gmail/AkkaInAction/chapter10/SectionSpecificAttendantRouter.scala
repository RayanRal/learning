package com.gmail.AkkaInAction.chapter10

import akka.actor.{ActorSystem, Props, SupervisorStrategy}
import akka.dispatch.Dispatchers
import akka.routing.{Router, RouterConfig}
import com.gmail.AkkaInAction.chapter7.LeadFlightAttendantProvider

/**
 * Created by rayanral on 19/06/15.
 */
//class SectionSpecificAttendantRouter extends RouterConfig {
//  this: LeadFlightAttendantProvider  =>
//
//  def routerDispatcher: String = Dispatchers.DefaultDispatcherId
//
//  def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.defaultStrategy
//
//  def createRoute(routeeProps: Props,
//                   routeeProvider: RouteeProvider): Route = {
//    val attendants = (1 to 5) map { n =>
//    routeeProvider.co
//    }
//  override def createRouter(system: ActorSystem): Router = ???

//}

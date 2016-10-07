package com.gmail.effectiveAkka.extra

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.gmail.effectiveAkka.{AccountBalances, GetCustomerAccountBalances}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by rayanral on 05/07/15.
 */
class AccountBalanceRetrieverVer1(savingsAccounts: ActorRef,
                               checkingAccounts: ActorRef,
                               moneyMarketAccounts: ActorRef) extends Actor {

  var originalSender: Option[ActorRef] = None

  implicit val timeout: Timeout = 100 milliseconds

  implicit val ec: ExecutionContext = context.dispatcher

  def receive = {
    case GetCustomerAccountBalances(id) =>
      val futSavings = savingsAccounts ? GetCustomerAccountBalances(id)
      val futChecking = checkingAccounts ? GetCustomerAccountBalances(id)
      val futMM = moneyMarketAccounts ? GetCustomerAccountBalances(id)
      val futBalances = for {
        savings <- futSavings.mapTo[Option[List[(Long, BigDecimal)]]]
        checking <- futChecking.mapTo[Option[List[(Long, BigDecimal)]]]
        mm <- futMM.mapTo[Option[List[(Long, BigDecimal)]]]
      } yield AccountBalances(savings, checking, mm)
      futBalances.foreach(sender ! _)
  }


}

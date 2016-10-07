package com.gmail.effectiveAkka.extra

import akka.actor.{Actor, ActorRef}
import akka.util.Timeout
import com.gmail.effectiveAkka.{AccountBalances, GetCustomerAccountBalances}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by rayanral on 05/07/15.
 */
class AccountBalanceRetrieverVer2(savingsAccounts: ActorRef,
                                  checkingAccounts: ActorRef,
                                  moneyMarketAccounts: ActorRef) extends Actor {

  val checkingBalances,
  savingBalances,
  mmBalances: Option[List[(Long, BigDecimal)]] = None

  var originalSender: Option[ActorRef] = None

  implicit val timeout: Timeout = 100 milliseconds

  implicit val ec: ExecutionContext = context.dispatcher

  def receive = {
    case GetCustomerAccountBalances(id) =>
      savingsAccounts ! GetCustomerAccountBalances(id)
      checkingAccounts ! GetCustomerAccountBalances(id)
      moneyMarketAccounts ! GetCustomerAccountBalances(id)
    case AccountBalances(cBalances, sBalances, mBalances) =>
      (checkingBalances, savingBalances, mmBalances) match {
        case(Some(c), Some(s), Some(m)) => originalSender.get ! AccountBalances(checkingBalances, savingBalances, mmBalances)
        case _ =>
      }
  }

}

package com.gmail.effectiveAkka.extra

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import com.gmail.effectiveAkka._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by rayanral on 05/07/15.
 */
class AccountBalanceRetrieverVer3(savingsAccounts: ActorRef,
                                  checkingAccounts: ActorRef,
                                  moneyMarketAccounts: ActorRef) extends Actor {

  val checkingBalances,
  savingBalances,
  mmBalances: Option[List[(Long, BigDecimal)]] = None

  implicit val timeout: Timeout = 100 milliseconds

  implicit val ec: ExecutionContext = context.dispatcher

  def receive = {
    case GetCustomerAccountBalances(id) =>
      context.actorOf(Props(new Actor {
        var checkingBalances,
        savingBalances,
        mmBalances: Option[List[(Long, BigDecimal)]] = None

        val originalSender = sender()

        override def receive: Receive = {
          case CheckingAccountBalances(balances) =>
            checkingBalances = balances
            isDone()
          case SavingsAccountBalances(balances) =>
            savingBalances = balances
            isDone()
          case MoneyMarketAccountBalances(balances) =>
            mmBalances = balances
            isDone()
        }

        def isDone() = (checkingBalances, savingBalances, mmBalances) match {
          case (Some(c), Some(s), Some(m)) =>
            originalSender ! AccountBalances(checkingBalances, savingBalances, mmBalances)
            context.stop(self)
          case _ =>
        }

        savingsAccounts ! GetCustomerAccountBalances(id)
        checkingAccounts ! GetCustomerAccountBalances(id)
        moneyMarketAccounts ! GetCustomerAccountBalances(id)
      }))
  }

}

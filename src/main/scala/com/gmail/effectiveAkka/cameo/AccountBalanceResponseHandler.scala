package com.gmail.effectiveAkka.cameo

import akka.actor.{Actor, ActorLogging, Props, ActorRef}
import akka.event.LoggingReceive
import akka.util.Timeout
import com.gmail.effectiveAkka._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 06/07/15.
 */
object AccountBalanceResponseHandler {

  def props(savingAccounts: ActorRef,
            checkingAccounts: ActorRef,
             moneyMarketAccounts: ActorRef,
            originalSender: ActorRef): Props = {
    Props(new AccountBalanceResponseHandler(savingAccounts, checkingAccounts, moneyMarketAccounts, originalSender))
  }
}

class AccountBalanceResponseHandler(savingAccounts: ActorRef,
                                    checkingAccounts: ActorRef,
                                    moneyMarketAccounts: ActorRef,
                                    originalSender: ActorRef) extends Actor with ActorLogging {

  var checkingBalances,
      savingsBalances,
      mmBalances: Option[List[(Long, BigDecimal)]] = None


  def receive = LoggingReceive {
    case CheckingAccountBalances(balances) =>
      log.debug(s"Received CheckingAccountBalances: $balances")
      checkingBalances = balances
      collectBalances()
    case SavingsAccountBalances(balances) =>
      log.debug(s"Received SavingsAccountBalances: $balances")
      savingsBalances = balances
      collectBalances()
    case MoneyMarketAccountBalances(balances) =>
      log.debug(s"Received MoneyMarketAccountBalances: $balances")
      mmBalances = balances
      collectBalances()
    case AccountRetrievalTimeout =>
      sendResponseAndShutdown(AccountRetrievalTimeout)
  }

  def collectBalances() = (checkingBalances, savingsBalances, mmBalances) match {
    case (Some(c), Some(s), Some(m)) =>
      log.debug("Values receive for all three account types")
      timoutMessager.cancel()
      sendResponseAndShutdown(AccountBalances(checkingBalances, savingsBalances, mmBalances))
    case _ =>
  }

  def sendResponseAndShutdown(response: Any) = {
    originalSender ! response
    log.debug("Stopping context capturing actor")
    context.stop(self)
  }

  val timoutMessager = context.system.scheduler.scheduleOnce(250 millis) {
    self ! AccountRetrievalTimeout
  }

}

class AccountBalanceRetriever(savingsAccounts: ActorRef,
                                checkingAccounts: ActorRef,
                                moneyMarketAccounts: ActorRef) extends Actor {

  implicit val timeout: Timeout = 100 milliseconds

  implicit val ec: ExecutionContext = context.dispatcher

  def receive = {
    case GetCustomerAccountBalances(id) =>
      val originalSender = sender()

      val handler = context.actorOf(
          AccountBalanceResponseHandler.props(savingsAccounts,
                                              checkingAccounts,
                                              moneyMarketAccounts,
                                              originalSender), "cameo-message-handler")

      savingsAccounts.tell(GetCustomerAccountBalances(id), handler)
      checkingAccounts.tell(GetCustomerAccountBalances(id), handler)
      moneyMarketAccounts.tell(GetCustomerAccountBalances(id), handler)
  }

}
package com.gmail.effectiveAkka.extra

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive
import akka.util.Timeout
import com.gmail.effectiveAkka._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by rayanral on 05/07/15.
 */
class AccountBalanceRetrieverVerFinal(savingsAccounts: ActorRef,
                                  checkingAccounts: ActorRef,
                                  moneyMarketAccounts: ActorRef) extends Actor with ActorLogging {

  implicit val timeout: Timeout = 100 milliseconds

  implicit val ec: ExecutionContext = context.dispatcher

  def receive = {
    case GetCustomerAccountBalances(id) =>
      log.debug(s"Received GetCustomerAccountBalances for ID: $id")
      val originalSender = sender()
      context.actorOf(Props(new Actor {
        var checkingBalances,
        savingBalances,
        mmBalances: Option[List[(Long, BigDecimal)]] = None

        def receive = LoggingReceive {
          case CheckingAccountBalances(balances) =>
            log.debug(s"Received CheckingAccountBalances: $balances")
            checkingBalances = balances
            collectBalances()
          case SavingsAccountBalances(balances) =>
            log.debug(s"Received SavingsAccountBalances: $balances")
            savingBalances = balances
            collectBalances()
          case MoneyMarketAccountBalances(balances) =>
            log.debug(s"Received MoneyMarketAccountBalances: $balances")
            mmBalances = balances
            collectBalances()
          case AccountRetrievalTimeout =>
            sendResponseAndShutdown(AccountRetrievalTimeout)
        }

        def collectBalances() = (checkingBalances, savingBalances, mmBalances) match {
          case (Some(c), Some(s), Some(m)) =>
            log.debug("Values receive for all three account types")
            timoutMessager.cancel()
            sendResponseAndShutdown(AccountBalances(checkingBalances, savingBalances, mmBalances))
          case _ =>
        }

        def sendResponseAndShutdown(response: Any) = {
          originalSender ! response
          log.debug("Stopping context capturing actor")
          context.stop(self)
        }

        savingsAccounts ! GetCustomerAccountBalances(id)
        checkingAccounts ! GetCustomerAccountBalances(id)
        moneyMarketAccounts ! GetCustomerAccountBalances(id)


        val timoutMessager = context.system.scheduler.scheduleOnce(250 millis) {
          self ! AccountRetrievalTimeout
        }
      }))
  }

}

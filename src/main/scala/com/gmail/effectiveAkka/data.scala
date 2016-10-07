package com.gmail.effectiveAkka

/**
 * Created by rayanral on 05/07/15.
 */
case class GetCustomerAccountBalances(id: Long)

case class AccountBalances(checking: Option[List[(Long, BigDecimal)]],
                            savings: Option[List[(Long, BigDecimal)]],
                            moneyMarket: Option[List[(Long, BigDecimal)]])

case class CheckingAccountBalances(balances: Option[List[(Long, BigDecimal)]])

case class SavingsAccountBalances(balances: Option[List[(Long, BigDecimal)]])

case class MoneyMarketAccountBalances(balances: Option[List[(Long, BigDecimal)]])

case object AccountRetrievalTimeout

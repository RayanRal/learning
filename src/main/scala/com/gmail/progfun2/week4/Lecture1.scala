package com.gmail.progfun2.week4

/**
  * Created by rayanral on 9/5/16.
  */
object Lecture1 extends App {

  trait Publisher {
    private var subscribers: Set[Subscriber] = Set.empty

    def subscribe(s: Subscriber) = subscribers = subscribers + s

    def unsubscribe(s: Subscriber) = subscribers = subscribers - s

    def publish(): Unit = subscribers.foreach(_.handler(this))
  }

  trait Subscriber {

    def handler(p: Publisher)

  }

  class BankAccountPub extends Publisher{

    private var balance = 0

    def currentBalance = balance

    def withdraw(amount: Int) = {
      if(balance > amount) {
        balance -= amount
        publish()
        true
      } else false
    }

    def deposit(amount: Int) = {
      balance += amount
      publish()
    }

  }

  class Consolidator(accounts: List[BankAccountPub]) extends Subscriber {
    accounts.foreach(_.subscribe(this))

    private var total: Int = _
    compute()

    def compute() = total = accounts.map(_.currentBalance).sum

    def handler(p: Publisher): Unit = compute()

    def totalBalance: Int = total
  }

}

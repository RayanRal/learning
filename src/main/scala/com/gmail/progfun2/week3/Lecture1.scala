package com.gmail.progfun2.week3

/**
  * Created by rayanral on 9/1/16.
  */
object Lecture1 extends App {



}


class BankAccount {

  private var balance = 0

  def withdraw(amount: Int) = {
    if(balance > amount) {
      balance -= amount
      true
    } else false
  }

  def deposit(amount: Int) = balance += amount

}
package com.gmail.progfun2.week3

/**
  * Created by rayanral on 9/1/16.
  */
object Lecture3 extends App {

  def power(x: Double, exp: Int): Double = {
    var r = 1.0
    var i = exp
    while (i > 0) {
      r = r * x
      i-=1
    }
    r
  }

  def WHILE(condition: => Boolean)(operation: => Unit): Unit = {
    if (condition) {
      operation
      WHILE(condition)(operation)
    } else {}
  }

  def REPEAT(command: => Unit)(condition: => Boolean): Unit = {
    command
    if(!condition) REPEAT(command)(condition) else {}
  }

}

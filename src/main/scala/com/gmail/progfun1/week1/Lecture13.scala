package com.gmail.progfun1.week1

import scala.annotation.tailrec

/**
  * Created by rayanral on 7/17/16.
  */
object Lecture13 extends App {

  def loop: Int = loop

  def firstCBN(x: Int, y: => Int) = x

  //`y` will be evaluated on every call
  def firstCBV(x: Int, y: Int) = x //standard choice, everything evaluated on function call


  firstCBN(1, loop) //will terminate, as `loop` is never called

  firstCBV(1, loop) //will never terminate, trying to evaluate `loop`

}

object Lecture14 extends App {


  def loop: Int = loop

  def and(x: Boolean, y: => Boolean) = if (x) y else false

  def or(x: Boolean, y: => Boolean) = if (x) true else y

}

object Lecture15 extends App {

  def abs(d: Double) = if(d < 0) -d else d

  def sqrt(target: Double): Double = {

    def isGoodEnough(guess: Double): Boolean =
      abs((guess * guess) - target)  / target < 0.01

    def sqrtIter(guess: Double): Double =
      if (isGoodEnough(guess)) guess
      else sqrtIter(improve(guess))

    def improve(guess: Double): Double = {
      (target + target / guess) / 2
    }

    sqrtIter(1)
  }

}

object Lecture17 extends App {

  def factorial(n: Int): Int = if(n == 0) 1 else n * factorial(n-1)

  def trFactRunner(n: Int) = tailRecFactorial(n, 1)
  @tailrec
  def tailRecFactorial(n: Int, accum: Int): Int = if(n == 0) accum else tailRecFactorial(n - 1, accum * n)

  //3! = 3*2*1 = 6
  //4! = 4*3*2*1 = 24
  //1! = 1
  //0! = 1

  println(trFactRunner(0))

}

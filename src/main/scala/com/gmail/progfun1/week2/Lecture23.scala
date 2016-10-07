package com.gmail.progfun1.week2

/**
  * Created by rayanral on 8/1/16.
  */
object Lecture23 extends App {

  val tolerance = 0.0001
  def isCloseEnough(x: Double, y: Double) = Math.abs((x - y) / x) / x < tolerance

  def fixedPoint(f: Double => Double)(firstGuess: Double) = {
    def iterate(guess: Double): Double = {
      val next = f(guess)
      if(isCloseEnough(guess, next)) guess
      else iterate(next)
    }
    iterate(firstGuess)
  }

  def averageDamp(f: Double => Double)(x: Double) = (x + f(x)) / 2

//  def sqrt(x: Double) = fixedPoint(y => (y + x / y) / 2)(1.0)
  def sqrt(x: Double) = fixedPoint(averageDamp(d => x / d))(1.0)

}

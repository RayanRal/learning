package com.gmail.progfun1.week2

/**
  * Created by rayanral on 8/2/16.
  */

class Rational(x: Int, y: Int) {

  def this(x: Int) = this(x, 1) //second constructor

  private def gcd(a: Int, b: Int): Int = if(b == 0) a else gcd(b, a % b)

  def numer = x
  def denom = y

  def less(other: Rational25): Boolean = this.numer * other.denom < other.numer * this.denom

//  def max(other: Rational25): Rational25 = if(this.less(other)) other else this

  def add(other: Rational25): Rational25 =
    new Rational25(
      this.numer * other.denom + other.numer * this.denom,
      this.denom * other.denom
    )

  def neg: Rational25 = new Rational25(-numer, denom)

  def subs(other: Rational25): Rational25 = add(other.neg)

  override def toString: String = {
    val g = gcd(x, y)
    (numer / g) + "/" + (denom / g)
  }
}

object Lecture26 {

}

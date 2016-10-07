package com.gmail.progfun1.week2

/**
  * Created by rayanral on 8/1/16.
  */
class Rational25(x: Int, y: Int) {
  def numer = x
  def denom = y

  def add(other: Rational25) =
    new Rational25(
      this.numer * other.denom + other.numer * this.denom,
      this.denom * other.denom
    )

  def neg = new Rational25(-numer, denom)

  def subs(other: Rational25) = add(other.neg)

  override def toString: String = numer + "/" + denom
}

object Lecture25 {

  val Rational = new Rational25(1, 2)

}

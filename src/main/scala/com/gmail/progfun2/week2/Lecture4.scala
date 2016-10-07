package com.gmail.progfun2.week2

/**
  * Created by rayanral on 8/30/16.
  */
object Lecture4 extends App {

  def from(n: Int): Stream[Int] = n #:: from(n + 1)

  val nat = from(0)
  val m4s = nat map (_ * 4)

  def sieve(stream: Stream[Int]): Stream[Int] = stream.head #:: sieve(stream.tail filter(_ % stream.head != 0))

}

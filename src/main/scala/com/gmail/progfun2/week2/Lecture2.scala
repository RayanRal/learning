package com.gmail.progfun2.week2

/**
  * Created by rayanral on 8/30/16.
  */
object Lecture2  extends App {

  val xs = Stream.cons(1, Stream.cons(2, Stream.empty))
  Stream(1, 2, 3)
  (1 to 1000).toStream

  def streamRange(from: Int, to: Int): Stream[Int] = {
    if(from >= to) Stream.empty
    else Stream.cons(from, streamRange(from + 1, to))
  }


}

package com.gmail.parprog1.week2

import com.gmail.parprog1.week1.Lect7
import com.gmail.parprog1.week1.Lect7.parallel

/**
  * Created by rayanral on 9/22/16.
  */
object Lect2 extends App {

  val threshold: Int = 10

  def mapASegSeq[A, B](in: Array[A], left: Int, right: Int, f: A => B, out: Array[B]) = {
    var i = left
    while(i < right) {
      out(i) = f(in(i))
      i += 1
    }
  }

  def mapASegPar[A, B](in: Array[A], left: Int, right: Int, f: A => B, out: Array[B]): Unit = {
    if(right - left < threshold)
      mapASegSeq(in, left, right, f, out)
    else {
      val mid = left + (right - left)  / 2
      parallel(
        mapASegPar(in, left, mid, f, out),
        mapASegPar(in, mid, right, f, out)
      )
    }
  }

}

package com.gmail.parprog1.week1

import scala.util.Random

/**
  * Created by rayanral on 9/16/16.
  */
object Lect6 extends App {

  def mcCount(iter: Int): Int = {
    val randomX = new Random()
    val randomY = new Random()
    var hits = 0
    for (i <- 0 until iter) {
      val x = randomX.nextDouble()
      val y = randomY.nextDouble()
      if(x * x + y * y < 1) hits += 1 //point is inside the circle
    }
    hits
  }

  def monteCarloPiSeq(iter: Int): Double = 4.0 * mcCount(iter) / iter

}

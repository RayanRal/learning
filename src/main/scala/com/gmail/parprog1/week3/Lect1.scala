package com.gmail.parprog1.week3

/**
  * Created by rayanral on 9/27/16.
  */
object Lect1 extends App {

  def initializeArray(xs: Array[Int])(v: Int): Unit = {
    for (i <- xs.indices.par) {
      xs(i) = v
    }
  }

}

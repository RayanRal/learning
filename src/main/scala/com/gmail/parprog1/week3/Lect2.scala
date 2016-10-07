package com.gmail.parprog1.week3

/**
  * Created by rayanral on 9/27/16.
  */
object Lect2 extends App {


  (1 until 10000).par.filter(_ % 3 == 0).count(n => n.toString == n.toString.reverse)

  def sum(xs: Array[Int]): Int = xs.par.foldLeft(0)(_ + _)

  val xs: Array[Int] = Array.emptyIntArray


}

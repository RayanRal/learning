package com.gmail.parprog1.week3

/**
  * Created by rayanral on 9/27/16.
  */
object Lect3 extends App {

  def max(xs: Array[Int]): Int = {
    xs.par.fold(Integer.MIN_VALUE)((x1, x2) => if(x1 > x2) x1 else x2)
  }

  def isVowel(c: Char): Boolean = ???
  Array('E', 'P', 'F', 'L').par.aggregate(0)((count: Int, c: Char) => if(isVowel(c)) count + 1 else count, _ + _)

}

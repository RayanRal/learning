package com.gmail.sedgeAlgo.ch1

/**
  * Created by rayanral on 5/13/16.
  */
object BinarySearch extends App {

  def rank(key: Int, a: Array[Int]): Int = {
    var lo = 0
    var hi = a.length - 1
    while (lo <= hi) {
      var mid = lo + (hi - lo) / 2
      if (key < a(mid)) hi = mid - 1
      if (key > a(mid)) lo = mid + 1
      else return mid
    }
    -1
  }

}

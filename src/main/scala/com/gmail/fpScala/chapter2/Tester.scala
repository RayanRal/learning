package com.gmail.fpScala.chapter2

import scala.annotation.tailrec

/**
 * Created by rayanral on 13/07/15.
 */
object Tester extends App {

  def binarySearch[A](ds: Array[A], key: A, gt: (A, A) => Boolean): Int = {

    @annotation.tailrec
    def go(low: Int, mid: Int, high: Int): Int = {
      if(low > high) -mid - 1
      else {
        val mid2 = (low + high) / 2
        val a = ds(mid2)
        val greater = gt(a, key)
        if(!greater && !gt(key, a)) mid2
        else if (greater) go(low, mid2, mid2-1)
        else go(mid2 + 1, mid2, high)
      }
    }

    go(0, 0, ds.length - 1)
  }


  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {

    @annotation.tailrec
    def go(index: Int): Boolean = {
      if(index + 1 > as.length) true
      else {
        val lessThan = gt(as(index + 1), as(index))
        if (lessThan) go(index + 1)
        else false
      }
    }

    go(0)
  }

  def ex1Fib(n: Int): Int = {
    @tailrec
    def go(prev: Int, cur: Int, curIndex: Int): Int = {
      if(curIndex == n) cur
      else go(cur, prev + cur, curIndex + 1)
    }

    go(0, 1, 1)
  }

}

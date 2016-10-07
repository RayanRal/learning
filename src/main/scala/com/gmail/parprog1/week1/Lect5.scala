package com.gmail.parprog1.week1

import scala.concurrent.Future

/**
  * Created by rayanral on 9/16/16.
  */
object Lect5 extends App {

  def sumSegment(a: Array[Int], p: Double, s: Int, t: Int): Double = {
    a.slice(s, t).map(x => Math.pow(x, p)).sum
  }

  val threshold = 10
  def segmentRec(a: Array[Int], p: Double, s: Int, t: Int): Double = {
    if(t - s  < threshold)
      sumSegment(a, p, s, t)
    else {
      val m = s + (t - s)/2
      val (sum1, sum2) = (segmentRec(a, p, s, m), segmentRec(a, p, m, t))
      sum1 + sum2
    }
  }

  def parallel[A, B](taskA: => A, taskB: => B): (A, B) = ???

  def pNorm(a: Array[Int], p: Double) = {
    Math.pow(sumSegment(a, p, 0, a.length), 1 / p)
  }

  def pNormTwoPart(a: Array[Int], p: Double) = {
    val m = a.length / 2
    val (sum1, sum2) = (sumSegment(a, p, 0, m), sumSegment(a, p, m, a.length))
    Math.pow(sum1 + sum2, 1/p)
  }

}

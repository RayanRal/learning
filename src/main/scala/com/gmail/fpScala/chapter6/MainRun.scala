package com.gmail.fpScala.chapter6

import java.util.Random

/**
 * Created by rayanral on 20/08/15.
 */
object MainRun extends App {

}


trait RNG {

  def nextInt: (Int, RNG)

  def nextInt(bound: Int): (Int, RNG)
}

object RNG {

  type Rand[+A] = RNG => (A, RNG)

  case class State[S, +A](run: S => (A, S))

  def simple(seed: Long): RNG = new RNG {
    def nextInt = {
      val seed2 = (seed * 0x5DEECE66DL + 0xBL) &
        ((1L << 48) - 1)
      ((seed2 >>> 16).asInstanceOf[Int],
        simple(seed2))
    }

    def nextInt(bound: Int): (Int, RNG) = (new Random().nextInt(bound), simple(seed * 0x5DEECE66DL + 0xBL))
  }

  def randomPair(rng: RNG): ((Int, Int), RNG) = {
    val (i1, rng2) = rng.nextInt
    val (i2, rng3) = rng2.nextInt
    ((i1, i2), rng3)
  }

  def positiveInt(rng: RNG): (Int, RNG) = {
    rng.nextInt match {
      case (Int.MinValue, rng2) => (Int.MaxValue, rng2)
      case (i, rng2) => (i.abs, rng2)
    }
  }

  def double(rng: RNG): (Double, RNG) = {
    val (i, rng2) = rng.nextInt
    (i.>>(2), rng2)
  }

//  def doubleMap(rng: RNG): (Double, RNG) =
    //val f = map()(i: Int => i.>>(2))//{
//    val (i, rng2) = rng.nextInt
//    (i.>>(2), rng2)
//  }

  def intDouble(rng: RNG): ((Int,Double), RNG) = {
    val (i, rng2) = rng.nextInt
    val (d, rng3) = double(rng2)
    ((i, d), rng3)
  }

  def doubleInt(rng: RNG): ((Double,Int), RNG) = {
    val ((i, d), rng2) = intDouble(rng)
    ((d, i), rng2)
  }

  def double3(rng: RNG): ((Double,Double,Double), RNG) = {
    val (d1, rng2) = double(rng)
    val (d2, rng3) = double(rng2)
    val (d3, rng4) = double(rng3)
    ((d1, d2, d3), rng4)
  }

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
    def go (r: RNG, count: Int): (List[Int], RNG) = count match {
      case 0 => (Nil, r)
      case x =>
        val (i, rng2) = r.nextInt
        val (list, rng3) = go(rng2, x-1)
        (i :: list, rng3)
    }
    go(rng, count)
  }

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] = rng => (a, rng)

  def map[S, A, B](a: S => (A, S))(f: A => B): S => (B, S) = {
    rng => {
      val resp = a(rng)
      (f(resp._1), resp._2)
    }
  }

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = {
    rng => {
      val (a, ra2) = ra(rng)
      val (b, rb2) = rb(ra2)
      (f(a, b), rb2)
    }
  }

//  def positiveMax(n: Int): Rand[Int] = map(int)

//  def positiveInt: Rand[Int] = map(int) {
//
//  }
}

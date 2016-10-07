package com.gmail.progfun1.week2

/**
  * Created by rayanral on 7/31/16.
  */
object Lecture22 extends App {

  def fact(x: Int): Int = if(x == 0) 1 else x * fact(x - 1)

  def sum(f: Int => Int)(a: Int, b: Int): Int = {
    if(a > b) 0 else f(a) + sum(f)(a + 1, b)
  }

//  def sumInts  = sum(x => x)
//  def sumCubes = sum(x => x * x * x)
//  def sumFacts = sum(fact)


  def prod(f: Int => Int)(a: Int, b: Int): Int =
    if(a > b) 1 else f(a) * prod(f)(a + 1, b)

  def factProd(x: Int): Int = prod(x => x)(1, x)


  //general, map-reduce version of sum and product
  def gen(f: Int => Int, combine: (Int, Int) => Int, unit: Int)(a: Int, b: Int): Int =
    if    (a > b) unit
    else  combine(f(a), gen(f, combine, unit)(a + 1, b))

}

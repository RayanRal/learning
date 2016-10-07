package com.gmail.progfun1.week2

/**
  * Created by rayanral on 7/31/16.
  */
object Lecture21 extends App {

  //without passing a function
  def id(x: Int) = x
  def sumInts(a: Int, b: Int): Int = if(a > b) 0 else  a + sumInts(a + 1, b)

  def cube(x: Int) = x * x * x
  def sumCubes(a: Int, b: Int): Int = if(a > b) 0 else cube(a) + sumCubes(a + 1, b)

  def fact(x: Int): Int = if(x == 0) 1 else x * fact(x - 1)
  def sumFacts(a: Int, b: Int): Int = if(a > b) 0 else fact(a) + sumFacts(a + 1, b)


  //with passing a function
  def sum(f: Int => Int, a: Int, b: Int): Int = if(a > b) 0 else f(a) + sum(f, a + 1, b)

  def fSumInts(a: Int, b: Int)  = sum(id, a, b)
  def fSumCubes(a: Int, b: Int) = sum(cube, a, b)
  def fSumFacts(a: Int, b: Int) = sum(fact, a, b)

  //with anonymous functions
  def anSumCubes(a: Int, b: Int) = sum((x: Int) => x * x * x, a, b)
  def anSumInts(a: Int, b: Int)  = sum((x: Int) => x, a, b)

  //tail-rec sum
  def tailSum(f: Int => Int)(a: Int, b: Int): Int = {
    def loop(a: Int, acc: Int): Int = {
      if(a > b) acc
      else loop(a + 1, f(a) + acc)
    }

    loop(a, 0)
  }

  println(fSumCubes(0, 3))

}

package com.gmail.progfun1.week3

/**
  * Created by rayanral on 8/7/16.
  */

object Lecture33 extends App {

  sealed trait List2[T] {
    def isEmpty: Boolean
    def head: T
    def tail: List2[T]
  }

  class Cons2[T](val head: T, val tail: List2[T]) extends List2[T] {
    def isEmpty: Boolean = false
  }

//  class Nill[T] extends List2[T] {
//    def isEmpty: Boolean = true

//    def head: Nothing = throw new NoSuchElementException

//    def tail: List2[Nothing] = throw new NoSuchElementException
//  }


//  def singleton[T](elem: T) = new Cons2[T](elem, new Nill)

//  def nth[T](n: Int, list: List2[T]): T = list match {
//    case Cons2(v, t) if n == 0 => v
//    case Cons2(v, t) if n != 0 => nth(n-1, t)
//    case Nill => throw new IndexOutOfBoundsException
//   }

  def nth2[T](n: Int, list: List2[T]): T =
    if (list.isEmpty) throw new IndexOutOfBoundsException
    else if(n == 0) list.head
    else nth2(n - 1, list.tail)

}

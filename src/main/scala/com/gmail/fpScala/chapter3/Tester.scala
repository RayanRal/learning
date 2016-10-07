package com.gmail.fpScala.chapter3

/**
 * Created by rayanral on 24/07/15.
 */

sealed trait List[+A]

case object Nil extends List[Nothing]

case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {

  def sum(ints: List[Int]): Int = foldLeft(ints, 0)(_ + _)

  def product(ds: List[Double]): Double = foldLeft(ds, 1.0)(_ * _)

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  def tail[A](as: List[A]): List[A] = drop(as, 1)

  def drop[A](as: List[A], elems: Int): List[A] = as match {
    case Cons(x, xs) if elems == 0 => xs
    case Nil => Nil
    case Cons(x, xs) => drop(xs, elems - 1)
  }

  def dropWhile[A](as: List[A])(f: A => Boolean): List[A] = as match {
    case Cons(x, xs) if f(x) => dropWhile(xs)(f)
    case Cons(x, xs) => xs
    case Nil => Nil
  }

  def setHead[A](as: List[A], head: A): List[A] = as match {
    case Cons(x, xs) => Cons(head, xs)
    case Nil => Cons(head, Nil)
  }

  def init[A](as: List[A]): List[A] = as match {
    case Cons(x, Nil) => Nil
    case Nil => Nil
    case Cons(x, xs) => Cons(x, init(xs))
  }

  def foldRight[A, B](l: List[A], z: B)(f: (A, B) => B): B = l match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

  def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = l match {
    case Nil => z
    case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
  }

  def length[A](l: List[A]): Int = foldLeft(l, 0)((b, a) => b + 1)

//  def append[A](l: List[A], elem: A)

  def map[A,B](l: List[A])(f: A => B): List[B] = l match {
    case Nil => Nil
    case Cons(x, xs) => Cons(f(x), map(xs)(f))
  }

  def toStringer[A](l: List[A]): List[String] = map(l)(_.toString)

  def pluser(l: List[Int]): List[Int] = map(l)(_ + 1)

  def filter[A](l: List[A])(f: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(x, xs) => if(f(x)) Cons(x, filter(xs)(f)) else filter(xs)(f)
  }

//  def flatMap[A, B](l: List[A])(f: A => List[B]): List[B] = l match {
//    case Nil => Nil
//    case Cons(x, xs) => flatMap(f(x))
//  }

//  def zip(l1: List[Int], l2: List[Int]): List[Int] = for {
//    el1 <- l1
//    el2 <- l2
//  } yield el1 + el2
//
  val example = Cons(1, Cons(2, Cons(3, Nil)))
  val example2 = List(1, 2, 3)
  val total = sum(example)





}

object Tester extends App {

  val ex3 = Cons(1, Cons(2, Cons(3, Cons(4, Nil))))
  val ex4 = Cons(6, Cons(7, Cons(8, Cons(10, Nil))))

  println("Filtered:" + List.filter(ex3)(_ % 2 == 0))
//  println("Zipped:" + List.zip(ex3, ex4))

}

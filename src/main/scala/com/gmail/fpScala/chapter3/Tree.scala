package com.gmail.fpScala.chapter3

/**
 * Created by rayanral on 05/08/15.
 */
sealed trait Tree[+A]

case class Leaf[A](value: A) extends Tree[A]

case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  def size[A](t: Tree[A]): Int = {
    def go(t: Tree[A], acc: Int): Int = t match {
      case Leaf(_) => acc + 1
      case Branch(left, right) => go(left, acc) + go(right, acc) + 1
    }

    go(t, 0)
  }

  def maximum(t: Tree[Int]): Int = {
    def go(t: Tree[Int], maximum: Int): Int = t match {
      case Leaf(x) => x max maximum
      case Branch(left, right) => go(left, maximum) max go(right, maximum)
    }

    go(t, Int.MinValue)
  }

  def map[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(x) => Leaf(f(x))
    case Branch(left, right) => Branch(map(left)(f), map(right)(f))
  }

  def depth[A](t: Tree[A]): Int = {
    def go(t: Tree[A], acc: Int): Int = t match {
      case Leaf(_) => acc + 1
      case Branch(left, right) => (go(left, acc) max go(right, acc)) + 1
    }

    go(t, 0)
  }

//  def fold[A, B](t: Tree[A], z: B)(f: (A, B) => B): B = {
//    def go(t: Tree[A], acc: B): B = t match {
//      case Leaf(v) => f(v, acc)
//      case Branch(left, right) => go(left, acc)(f)
//    }
//  }
}

object TestTree extends App {

  val testTree = Branch[Int](Branch[Int](Branch[Int](Leaf[Int](1), Leaf[Int](1)), Leaf[Int](5)), Leaf[Int](2))

  println(Tree.depth(testTree))

}
package com.gmail.fpScala.chapter5

/**
 * Created by rayanral on 16/08/15.
 */
object MainRunner extends App {

  def ifStrict[A](cond: Boolean, onTrue: A, onFalse: A): A =
    if(cond) onTrue
    else onFalse

  def ifLazy[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
    if(cond) onTrue
    else onFalse

//  println(s"Strict: ${ifStrict[Integer](true, 2, {sys.error("Some!"); 3})}")
  println(s"Lazy: ${ifLazy[Integer](true, 2, {sys.error("Some!"); 3})}")

  val ones: Stream[Int] = Stream.cons(1, ones)
}


trait Stream[+A]{

  def uncons: Option[(A, Stream[A])]
  def isEmpty: Boolean = uncons.isEmpty

  def toList: List[A] = uncons.map(cell => cell._1 :: cell._2.toList).getOrElse(Nil)

  def take(n: Int): Stream[A] = n match {
    case 0 => Stream.empty
    case count => this.uncons match {
      case None => Stream.empty
      case Some((h, t)) => Stream.cons(h, t.take(count - 1))
    }
  }

  def takeWhile(p: A => Boolean): Stream[A] = uncons match {
    case None => Stream.empty
    case Some((h, t)) if p(h) => Stream.cons(h, t.takeWhile(p))
    case Some((h, t)) => Stream.empty
  }

  def foldRight[B](z: => B)(f: (A, =>B) => B): B = uncons match {
    case None => z
    case Some((h, t)) => f(h, t.foldRight(z)(f))
  }

  def exists(p: A => Boolean): Boolean = foldRight(false)((a, b) => p(a) || b)

  def forAll(p: A => Boolean): Boolean = foldRight(false)((a, b) => p(a) && b)

  def takeWhile2(p: A => Boolean): Stream[A] = foldRight(Stream.empty[A])((a, b) => if(p(a)) Stream.cons(a, b) else Stream.empty[A])
}

object Stream {

  def empty[A]: Stream[A] = new Stream[A] { def uncons = None }

  def cons[A](head: => A, tail: => Stream[A]): Stream[A] = new Stream[A] {
    lazy val uncons = Some((head, tail))
  }

  def apply[A](as: A*): Stream[A] =
    if(as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  def constant[A](a: A): Stream[A] = cons(a, constant(a))

  def from(n: Int): Stream[Int] = cons(n, from(n + 1))

  def summer(pprev: Int, prev: Int): Stream[Int] = cons(pprev + prev, summer(prev, prev + pprev))

  def fibs: Stream[Int] = summer(0, 1)

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] = f(z) match {
    case Some((elem, nextState)) => cons(elem, unfold(nextState)(f))
    case None => Stream.empty[A]
  }
}
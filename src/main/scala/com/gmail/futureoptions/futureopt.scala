package com.gmail.futureoptions

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 11/07/15.
 */
case class FutureOption[A](inner: Future[Option[A]]) {

  def map[B](f: A => B): FutureOption[B] = FutureOption {
    inner.map { _.map(f) }
  }

  def flatMap[B](f: A => FutureOption[B]): FutureOption[B] = FutureOption {
    inner.flatMap {
      case Some(a) => f(a).inner
      case None    => Future.successful(None)
    }
  }
}

trait Monad[M[_]] {

  def map[A, B](ma: M[A], f: A => B): M[B]
  def flatMap[A, B](ma: M[B], f: A => M[B]): M[B]
  def create[A](a: A): M[A]

}

case class AnyMonadOption[M[_], A](inner: M[Option[A]])(implicit m: Monad[M]) {

//  def map[B](f: A => B): AnyMonadOption[M, B] = AnyMonadOption {
//    m.map(inner, _.map { f } )
//  }

//  def flatMap[B](f: A => AnyMonadOption[M, B]): AnyMonadOption[M, B] = AnyMonadOption {
//    m.flatMap(inner) {
//      case Some(a) => f(a).inner
//      case None    => m.create(None)
//    }
//  }
}

object Tester extends App {

  def getX: Future[Option[Int]] = Future(Some(5))
  def getY: Future[Option[Int]] = Future(Some(3))

  val z1: FutureOption[Int] = for {
    x <- FutureOption[Int](getX)
    y <- FutureOption[Int](getY)
  } yield x + y

}
package com.gmail.fpScala.chapter7

import java.util.concurrent.{Callable, Future, ExecutorService}

import com.gmail.fpScala.chapter7.Par.Par

/**
 * Created by rayanral on 22/08/15.
 */
object MainRunner extends App {

  def sum(as: IndexedSeq[Int]): Par[Int] =
    if(as.size <= 1) Par.unit(as.headOption getOrElse 0)
    else {
      val (l, r) = as.splitAt(as.length / 2)
      Par.map2(Par.fork(sum(l)), Par.fork(sum(r)))(_ + _)
    }

  def f(x: Int): Int = x
  val x: Int = 5

  Par.map(Par.unit(x))(f) == Par.unit(f(x))
//  Par.map(x)(Par.id) == x
}


object Par {

  type Par[A] = ExecutorService => Future[A]

  def async[A](a: => A): Par[A] = fork(unit(a))

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  def unit[A](a: A): Par[A] = es => es.submit(new Callable[A] {
    override def call() = a
  })

  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = map(product(a, b))(res => f(res._1, res._2))

  def fork[A](a: => Par[A]): Par[A] = es => es.submit(new Callable[A] {
    override def call(): A = a(es).get()
  })

  def delay[A](a: => Par[A]): Par[A] = es => a(es)

  def asyncF[A, B](f: A => B): A => Par[B] = a => async(f(a))

  def sortPar(l: Par[List[Int]]): Par[List[Int]] = map(l)(_.sorted)

  def product[A,B](a: Par[A], b: Par[B]): Par[(A,B)] = es => {
    es.submit(new Callable[(A, B)] {
      override def call(): (A, B) = (a(es).get, b(es).get)
    })
  }

  def map[A, B](a: Par[A])(f: A => B): Par[B] = es => {
    es.submit(new Callable[B] {
      override def call(): B = f(a(es).get())
    })
  }

  def parMap[A,B](l: List[A])(f: A => B): Par[List[B]] = fork {
    val fbs: List[Par[B]] = l.map(asyncF(f))
    sequence(fbs)
  }

  def sequence[A](l: List[Par[A]]): Par[List[A]] = l.foldRight[Par[List[A]]](unit(List()))((h, t) => map2(h, t)(_ :: _))

  def parFilter[A](l: List[A])(f: A => Boolean): Par[List[A]] = fork {
    async(l.filter(f))
  }

  def equal[A](e: ExecutorService)(p1: Par[A], p2: Par[A]): Boolean =
    p1(e).get == p2(e).get

  def id[A](a: A): A = a

  def choice[A](a: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] =
    flatMap(a)(a => if(a) ifTrue else ifFalse)

  def choiceN[A](a: Par[Int])(choices: List[Par[A]]): Par[A] =
    flatMap(a)(choices)

  def flatMap[A,B](a: Par[A])(choices: A => Par[B]): Par[B] = es =>
    choices(a(es).get)(es)

  def join[A](a: Par[Par[A]]): Par[A] = es => {
    val r = a(es).get
    r(es)
  }

}

trait Applicative[F[_]] {
  def map2[A, B](a: F[A], b: F[B])(f: (A, B) => B): F[B]
}

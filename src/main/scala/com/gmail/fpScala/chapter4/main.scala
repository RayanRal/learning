package com.gmail.fpScala.chapter4

/**
 * Created by rayanral on 08/08/15.
 */
sealed trait Option[+A] {
  def map[B](f: A => B): Option[B]
  def flatMap[B](f: A => Option[B]): Option[B]

//  def getOrElse[B >: A](default: => B): B = {
//    case Some(a) => a
//    case None => default
//  }

//  def orElse[B >: A](ob: => Option[B]): Option[B] = {
//    case Some(a) => a
//    case None => ob
//  }

  def filter(f: A => Boolean): Option[A]
}

case class Some[+A](value: A) extends Option[A] {

  def map[B](f: A => B): Option[B] = Some(f(value))

  def flatMap[B](f: A => Option[B]): Option[B] = f(value)

  def filter(f: A => Boolean): Option[A] = if(f(value)) Some(value) else None
}

case object None extends Option[Nothing] {

  def map[B](f: Nothing => B): Option[B] = None

  def flatMap[B](f: Nothing => Option[B]): Option[B] = None

  def filter(f: Nothing => Boolean): Option[Nothing] = None

}


object TestOpt extends App {

  def mean(xs: Seq[Double]): Option[Double] = {
    if(xs.isEmpty) None
    else Some(xs.sum / xs.length)
  }

  import java.util.regex._
  def pattern(s: String): Option[Pattern] =
    try {
      Some(Pattern.compile(s))
    } catch {
      case e: PatternSyntaxException => None
    }

  def mkMatcher_1(pat: String): Option[String => Boolean] =
    for {
      p <- pattern(pat)
    } yield (s: String) => p.matcher(s).matches
  def doesMatch(pat: String, s: String): Option[Boolean] =
    for {
      p <- mkMatcher_1(pat)
    } yield p(s)


  def bothMatch(pat: String, pat2: String, s: String): Option[Boolean] =
    for {
      f <- mkMatcher_1(pat)
      g <- mkMatcher_1(pat2)
    } yield f(s) && g(s)

  def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    for {
      val1 <- a
      val2 <- b
    } yield f(val1, val2)
//  a.map(val1 => b.flatMap(val2 => f(val1, val2)))

  def bothMatch_2(pat1: String, pat2: String, s: String): Option[Boolean] =
    map2(mkMatcher_1(pat1), mkMatcher_1(pat2))((a, b) => a(s) && b(s))

//  def sequence[A](a: List[Option[A]]): Option[List[A]] = a.foldRight(Option){
//    case (_, None) => None
//    case (val1, Some(val2)) => Some(List(val1, val2))
//  }

}


sealed trait Either[+E, +A] {

  def map[B](f: A => B): Either[E, B]

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B]

  def orElse[EE >: E,B >: A](b: => Either[EE, B]): Either[EE, B]

//  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = b match {
//    case Left(ee) => Left(ee)
//    case Right(b) =>
//  }

}

case class Left[+E](value: E) extends Either[E, Nothing] {

  def map[B](f: Nothing => B): Either[E, B] = this

  def flatMap[EE >: E, B](f: Nothing => Either[EE, B]): Either[EE, B] = this

  def orElse[EE >: E,B >: Nothing](b: => Either[EE, B]): Either[EE, B] = b

}

case class Right[+A](value: A) extends Either[Nothing, A] {

  def map[B](f: A => B): Either[Nothing, B] = Right(f(value))

  def flatMap[EE >: Nothing, B](f: A => Either[EE, B]): Either[EE, B] = f(value)

  def orElse[EE >: Nothing,B >: A](b: => Either[EE, B]): Either[EE, B] = this

}
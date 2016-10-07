package com.gmail.fpScala.chapter9

/**
 * Created by rayanral on 10/29/15.
 */
trait Parsers[ParseError, Parser[+_]] {

  def char(c: Char): Parser[Char]

  implicit def string(s: String): Parser[String]

  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]]

  def or[A](p1: Parser[A], p2: Parser[A]): Parser[A]

  implicit def operators[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)

//  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps

  def run[A](p: Parser[A])(input: String): Either[ParseError, A]


  case class ParserOps[A](p: Parser[A]) {
    def | [B >: A](p2: Parser[B]): Parser[B] = or(p, p2)
//    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
  }

}

package com.gmail.parboiled

import org.parboiled2
import org.parboiled2.CharPredicate._
import org.parboiled2._
import shapeless.HNil

/**
  * Created by rayanral on 11/12/15.
  */
class MyParser(val input: ParserInput) extends Parser {

  def myCharRule = rule { 'a' }

  def myStringRule = rule { "string" }

  def stringWithCaseIgnored = rule { ignoreCase("string") }

  def digit = rule { "0" - "9" }

  def arithmeticOperation = rule { anyOf("+-/*^")}

  def bartLearningParboiled = rule { 100 times "I will never write a parser again" }

  def futureOfCxx = rule { 'C' ~ (2 to 5).times("+") } //c++ / c+++ / c+++++

  def whitespace = rule { anyOf("\n\t") }

  def whitespaceOpt = rule { zeroOrMore(whitespace) }

  def unsignedInteger = rule { oneOrMore(CharPredicate.Digit) }

  def commaSeparatedNumbers = rule { oneOrMore(unsignedInteger).separatedBy(",") }

  def simplifiedDateRule = rule { Year ~ "-" ~ Month ~ "-" ~ Date }
  def Year =  rule { digit ~ digit ~ digit ~ digit }
  def Month = rule { digit ~ digit }
  def Date =  rule { digit ~ digit }


  def newLine = rule { optional('\r') ~ '\n'}


  def signum = rule { "+" | "-" }
  def maybeSign = rule { optional(signum) }


  def Integer = rule { maybeSign ~ unsignedInteger }


  def myMainRule = rule { ignoreCase("match") ~ EOI }


  def FirstName: Rule0 = rule { CharPredicate.UpperAlpha ~ oneOrMore(CharPredicate.LowerAlpha) }
  def LastName: Rule0 = rule { CharPredicate.UpperAlpha ~ oneOrMore(CharPredicate.LowerAlpha) }
  def Separator = rule { anyOf("\n\t") }
  def User: Rule0 = rule { FirstName ~ Separator ~ LastName}
//  def UserCapture = rule {
//    capture(FirstName) ~ Separator ~ capture(LastName) ~>
//      ( (firstname, lastname) => firstname :: lastname :: HNil )
//  }


//  def UnsignedInteger: Rule1[Int] = rule {
//    capture(oneOrMore(CharPredicate.Digit)) ~> (i => i.toInt)
//  }

//  def TwoTimer = rule {
//    UnsignedInteger ~> ((i: Int) => i * 2)
//  }


}

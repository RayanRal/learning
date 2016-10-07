package com.gmail.parboiled

import org.parboiled2.{Rule0, CharPredicate, Parser, ParserInput}

/**
  * Created by rayanral on 11/24/15.
  */
class BKVParser(val input: ParserInput) extends Parser with SupportConstants {

  import SupportConstants._

  def oversimplifiedQuotedString = rule {
    '"' ~ zeroOrMore(allowedChar) ~ '"'
  }

  def allowedChar = rule { noneOf("\"\\") | escapeSequence }

  def escapeSequence = rule { "\\" ~ anyOf("\"\\nafv")}


  val WhitespaceChars = "\n\t "
  def WhiteSpace = rule { anyOf(WhitespaceChars) }
  def OptWs      = rule { zeroOrMore(WhiteSpace) }

  def Newline = rule { optional('\r') ~ '\n' }

  def Identifier = rule {
    IdentifierFirstChar ~ zeroOrMore(IdentifierChar)
  }

  def Key = rule { Identifier }

  def Value = rule { quotedString }

  def KeyValuePair = rule { Key ~ OptWs ~ "=" ~ OptWs ~ Value }

  def Node: Rule0 = rule { KeyValuePair | Block }

  def Nodes = rule {
    OptWs ~ zeroOrMore(Node).separatedBy(Newline ~ OptWs) ~ OptWs
  }

  def BlockName = rule { Identifier }

  def Block = rule { BlockName ~ BlockBeginning ~ Nodes ~ BlockEnding }

  def Root = rule { Nodes ~ EOI }

}


object SupportConstants {

  val charsToBeEscaped = "abfnrtv\\\""

  val backslash = '\\'

  val AllowedChars = CharPredicate.Printable -- backslash -- "\""

  val IdentifierFirstChar = CharPredicate.Alpha ++ '_'
  val IdentifierChar = CharPredicate.AlphaNum ++ '.' ++ '_'

  val BlockBeginning = '{'
  val BlockEnding = '}'

  val RootNodeName = ""
}

trait SupportConstants {
  this: Parser =>

  import SupportConstants._

  def quotedString = rule { '"' ~ QuotedStringContent ~ '"' }

  def QuotedStringContent = rule { oneOrMore(AllowedChars | DoubleQuotedStringEscapeSequence )}

  def DoubleQuotedStringEscapeSequence = rule { '\\' ~ anyOf(charsToBeEscaped) }

}
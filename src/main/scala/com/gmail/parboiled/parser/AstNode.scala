package com.gmail.parboiled.parser

import com.gmail.parboiled.SupportConstants
import org.parboiled2._

/**
  * Created by rayanral on 11/25/15.
  */
sealed trait AstNode {
  def name: String
}
case class KeyValueNode(override val name: String, value: String) extends AstNode
case class BlockNode(override val name: String, nodes: Seq[AstNode]) extends AstNode


class AstNodeParser(val input: ParserInput) extends Parser with SupportConstants {
  import SupportConstants._

  def Newline = rule { optional('\r') ~ '\n' }
  val WhitespaceChars = "\n\t "
  def WhiteSpace = rule { anyOf(WhitespaceChars) }
  def OptWs      = rule { zeroOrMore(WhiteSpace) }

  def Identifier = rule {
    IdentifierFirstChar ~ zeroOrMore(IdentifierChar)
  }

  def Key = rule {
    capture(Identifier)
  }

  def Value: Rule1[String] = rule {
    QuotedString
  }

  def QuotedString: Rule1[String] = rule {
    '"' ~ capture(QuotedStringContent)  ~ '"'
  }

  def KeyValuePair: Rule1[AstNode] = rule {
    Key ~ OptWs ~ "=" ~ OptWs ~ Value ~> KeyValueNode
  }

  def BlockName: Rule1[String] = rule { capture(Identifier) }
  def Block: Rule1[AstNode] = rule {
    BlockName ~ OptWs ~ BlockBeginning ~ Nodes ~ BlockEnding ~> BlockNode
  }

  def Node: Rule1[AstNode] = rule { KeyValuePair | Block }

  def Nodes: Rule1[Seq[AstNode]] = rule {
    OptWs ~ zeroOrMore(Node).separatedBy(Newline ~ OptWs) ~ OptWs
  }

  def Root: Rule1[AstNode] = rule {
    Nodes ~ EOI ~> { nodes: Seq[AstNode] => BlockNode(RootNodeName, nodes) }
  }


}
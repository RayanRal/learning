package com.gmail.parboiled.parser

import com.gmail.parboiled.SupportConstants

/**
  * Created by rayanral on 11/25/15.
  */
trait NodeAccessDsl {
  this: AstNode =>

  def isRoot = this.name == SupportConstants.RootNodeName

  lazy val isBlockNode = this match {
    case _: KeyValueNode => false
    case _               => true
  }

//  def pairs: Seq[KeyValueNode] = this match {
//    case a @ BlockNode(_, nodes) =>
//      nodes.collect { case node: KeyValueNode => node }
//    case _ => Seq.empty
//  }
//
//  def blocks: Seq[BlockNode] = this match {
//    case a @ BlockNode(_, nodes) =>
//      nodes.collect { case node : BlockNode => node }
//    case _ => Seq.empty
//  }
//
//  def getValue: Option[String] = this match {
//    case KeyValueNode(_, value) => Some(value)
//    case _ => None
//  }

}

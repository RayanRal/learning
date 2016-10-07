package com.gmail.typelevel

import shapeless.test.illTyped

/**
  * Typelevel programming 101: The Subspace of Scala - Joe Barnes
  */
sealed trait BoolType {

  type Not <: BoolType
  type Or[That <: BoolType]
  type \/[A <: BoolType, B <: BoolType] = A#Or[B]

}

sealed trait TrueType extends BoolType {

  override type Not = FalseType
  override type Or[That <: BoolType] = TrueType

}

sealed trait FalseType extends BoolType {

  override type Not = TrueType
  override type Or[That <: BoolType] = That

}

object BoolTypeSpecs {

  implicitly[TrueType =:= TrueType]
  implicitly[FalseType =:= FalseType]

  implicitly[TrueType#Not =:= FalseType]

  implicitly[TrueType#Or[FalseType] =:= TrueType]

  illTyped("implicitly[TrueType#Or[FalseType] =:= FalseType]") //shapeless - compiles only if string can't compile
}
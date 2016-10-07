package com.gmail.shapeless

import _root_.shapeless._

/**
  * Created by rayanral on 11/22/15.
  */
trait Depth[T] {

  def depth(t: T): Int

}

object Depth extends TypeClassCompanion[Depth] {

  object typeClass extends TypeClass[Depth] {

    override def emptyProduct = new Depth[HNil] {
      override def depth(t: HNil): Int = 0
    }

    override def product[F, R <: HList](sh: Depth[F], sr: Depth[R]) = new Depth[F :: R] {
      override def depth(t: F :: R): Int = {
        val head = sh.depth(t.head) + 1
        val tail = sr.depth(t.tail)
        Math.max(head, tail)
      }
    }

    override def emptyCoproduct = new Depth[CNil] {
      override def depth(t: CNil): Int = 0
    }

    override def coproduct[L, R <: Coproduct](sl: => Depth[L], sr: => Depth[R]) = new Depth[L :+: R] {
      override def depth(lr: L :+: R): Int = lr match {
        case Inl(l) => sl.depth(l)
        case Inr(r) => sr.depth(r)
      }
    }

    override def project[F, G](instance: => Depth[G], to: F => G, from: G => F) = new Depth[F] {
      override def depth(f: F): Int = instance.depth(to(f))
    }
  }

}

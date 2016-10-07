package com.gmail

import _root_.shapeless._

/**
  * Created by rayanral on 11/22/15.
  */
package object shapeless {

  //Depth usage examples
  implicit def stringDepth: Depth[String] = new Depth[String] {
    override def depth(t: String): Int = 1
  }

  implicit def intDepth: Depth[Int] = new Depth[Int] {
    override def depth(t: Int): Int = 1
  }

  implicit def listDepth[T](implicit elementDepth: Depth[T]): Depth[List[T]] = new Depth[List[T]] {
    override def depth(t: List[T]): Int = {
      if(t.isEmpty) 1
      else t.map(elementDepth.depth).max + 1
    }
  }


  //Calculating Depth without Shapeless
  implicit def coordinateDepth(implicit iDepth: Depth[Int]): Depth[Coordinate] = new Depth[Coordinate] {
    override def depth(t: Coordinate): Int = {
      val max = iDepth.depth(t.x) max iDepth.depth(t.y)
      max + 1
    }
  }

  implicit def rectangleDepth(implicit coordDepth: Depth[Coordinate]): Depth[Rectangle] = new Depth[Rectangle] {
    override def depth(t: Rectangle): Int = {
      val max = coordDepth.depth(t.corner1) max coordDepth.depth(t.corner2)
      max + 1
    }
  }

  implicit def circleDepth(implicit iDepth: Depth[Int], coordDepth: Depth[Coordinate]): Depth[Circle] = new Depth[Circle] {
    override def depth(t: Circle): Int = {
      iDepth.depth(t.radius) max coordDepth.depth(t.center) + 1
    }
  }

  implicit def triangleDepth(implicit coordDepth: Depth[Coordinate]): Depth[Triangle] = new Depth[Triangle] {
    override def depth(t: Triangle): Int = {
      val max = coordDepth.depth(t.corner1) max coordDepth.depth(t.corner2) max coordDepth.depth(t.corner2)
      max + 1
    }
  }

  implicit def shapeDepth(implicit cdepth: Depth[Circle], rdepth: Depth[Rectangle], tdepth: Depth[Triangle]): Depth[Shape] = new Depth[Shape] {
    override def depth(t: Shape): Int = {
      t match {
        case c: Circle => cdepth.depth(c)
        case r: Rectangle => rdepth.depth(r)
        case t: Triangle => tdepth.depth(t)
      }
    }
  }

  implicit def surfaceDepth(implicit sdepth: Depth[String], shapeDepth: Depth[Shape]): Depth[Surface] = new Depth[Surface] {
    override def depth(t: Surface): Int = {
      val max = sdepth.depth(t.name) max shapeDepth.depth(t.shape1) max shapeDepth.depth(t.shape2)
      max + 1
    }
  }





  //Depth with Shapeless
  implicit val hnilDepth: Depth[HNil] = new Depth[HNil] {
    override def depth(t: HNil): Int = 0
  }

  implicit def hlistConsDepth[F, R <: HList](implicit fdepth: Depth[F], rdepth: Depth[R]): Depth[F :: R] = new Depth[F :: R] {
    override def depth(t: F :: R): Int = {
      val head = fdepth.depth(t.head) + 1
      val tail = rdepth.depth(t.tail)
      Math.max(head, tail)
    }
  }


  implicit val cnilDepth: Depth[CNil] = new Depth[CNil] {
    override def depth(t: CNil): Int = 0
  }

  implicit def coproductConsDepth[L, R <: Coproduct](implicit ldepth: Depth[L], rdepth: Depth[R]): Depth[L :+: R] = new Depth[L :+: R] {
    override def depth(t: L :+: R): Int = t match {
      case Inl(l) => ldepth.depth(l)
      case Inr(r) => rdepth.depth(r)
    }
  }

}

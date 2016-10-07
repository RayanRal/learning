package com.gmail.parprog1.week4

/**
  * Created by rayanral on 9/27/16.
  */
object Lect3 {

}

sealed trait Conc[+T] {
  def level: Int
  def size: Int
  def left: Conc[T]
  def right: Conc[T]

//  def <>(that: Conc[T]): Conc[T] = {
//    if(this == Empty) that
//    if(that == Empty) this
//    else concat(this, that)
//  }

//  def concat(xs: Conc[T], ys: Conc[T]): Conc[T] = {
//    val diff = xs.level - ys.level
//    if(math.abs(diff) <= 1) new <>(xs, ys)
//    else if (diff < -1)
//  }
}

case object Empty extends Conc[Nothing] {
  def level = 0
  def size = 0
  def left: Conc[Nothing] = ???
  def right: Conc[Nothing] = ???
}

//leaf
class Single[T](val x: T) extends Conc[T] {
  def level: Int = 0
  def size: Int = 1
  def left: Conc[T] = ???
  def right: Conc[T] = ???
}

//inner node
case class <>[T](left: Conc[T], right: Conc[T]) extends Conc[T] {
  def level: Int = 1 + math.max(left.level, right.level)
  def size: Int = left.size + right.size
}
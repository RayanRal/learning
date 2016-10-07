package com.gmail.progfun2.week1

/**
  * Created by rayanral on 8/29/16.
  */
object Lecture4 {

  trait M[T] {
    def flatMap[U](f: T => M[U]): M[U]

//    def map[U](f: T => U): U = f
  }

//  def unit[T](x: T): M[T]


}

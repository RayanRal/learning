package com.gmail.parprog1.week4

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag

/**
  * Created by rayanral on 9/27/16.
  */
object Lect2 extends App {

}

trait Task[A] {

  def join: A

}

class ArrayCombiner[T <: AnyRef: ClassTag](val parallelism: Int) {
  private var numElems = 0
  private val buffers = new ArrayBuffer[ArrayBuffer[T]]
  buffers += new ArrayBuffer[T]

  def task[A](a: => A): Task[A] = ???

  def +=(elem: T) = {
    buffers.last += elem
    numElems += 1
    this
  }

  def copyTo(arr: Array[T], from: Int, to: Int) = ???

  def result: Array[T] = {
    val array = new Array[T](numElems)
    val step = math.max(1, numElems / parallelism)
    val starts = (0 until numElems by step) :+ numElems
    val chunks = starts.zip(starts.tail)
    val tasks = for ((from, end) <- chunks) yield task {
      copyTo(array, from, end)
    }
    tasks.foreach(_.join)
    array
  }
}
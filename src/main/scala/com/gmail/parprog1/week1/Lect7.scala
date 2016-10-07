package com.gmail.parprog1.week1

/**
  * Created by rayanral on 9/16/16.
  */
object Lect7 extends App {

  def task[A](a: => A): Task[A] = ???

  trait Task[A] {

    def join: A

  }

  def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
    val a = task(taskA)
    val b = task(taskB)
    (a.join, b.join)
  }

}

package com.gmail.AkkaInAction.chapter3

import java.util.concurrent.Executors

import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._

/**
 * Created by rayanral on 16/05/15.
 */
object MainAkka {

  val pool = Executors.newCachedThreadPool()
  implicit val ec = ExecutionContext.fromExecutor(pool)

  def main(args: Array[String]): Unit = {
    val future = Future { "Fibonacci numbers" }
    val result = Await.result(future, 1.second)
    println(result)
    pool.shutdown()
  }

}

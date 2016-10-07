package com.gmail.AkkaInAction.chapter4

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by rayanral on 17/05/15.
 */
object MatrixFuture {


  val pool = Executors.newCachedThreadPool()
  implicit val ec = ExecutionContext.fromExecutor(pool)

  val list = List(1, 2, 3)
  val grouped = list.grouped(500)
  private val futures: Iterator[Future[List[Int]]] = grouped.map(smallList => Future { for{x <- smallList} yield x + 2 })
  private val results: Future[Iterator[List[Int]]] = Future.sequence(futures)


}

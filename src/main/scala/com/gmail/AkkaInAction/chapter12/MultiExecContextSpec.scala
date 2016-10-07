package com.gmail.AkkaInAction.chapter12

import java.util.concurrent.Executors

import org.scalatest.{WordSpecLike, MustMatchers}

import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._

/**
 * Created by rayanral on 25/06/15.
 */
class MultiExecContextSpec extends WordSpecLike with MustMatchers {
  lazy val fibs: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibs.zip(fibs.tail).map {n => n._1 + n._2}

  "Future" should {
    "calculate fibonacci numbers" in {
      val execService = Executors.newCachedThreadPool()
      implicit val executionContext = ExecutionContext.fromExecutor(execService)

      val futureFib = Future{ fibs.drop(99).head }
      val fib = Await.result(futureFib, 1.second)

      fib mustBe BigInt("218922995834555169026")

    }
  }

}

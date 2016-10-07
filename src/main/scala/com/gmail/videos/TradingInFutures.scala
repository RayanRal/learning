package com.gmail.videos

import java.util.concurrent.ConcurrentLinkedQueue

import scala.concurrent._
import scala.util.Try
import scala.concurrent.duration._

/**
  * Created by rayanral on 7/15/16.
  * https://www.youtube.com/watch?v=K9lt6yjuzbM
  * Trading in Futures - By Viktor Klang
  */
object TradingInFutures extends App {

  class ManualEC extends ConcurrentLinkedQueue[Runnable] with ExecutionContext {

    override def execute(r: Runnable): Unit = this add r

    override def reportFailure(cause: Throwable): Unit = cause.printStackTrace(System.err)

    def interpNext(): Boolean = Option(poll()).fold(false) { r =>
      println(s"Interpreting next $r")
      r.run()
      !isEmpty
    }

  }

}

object Transformer extends App {

  val p = Promise[String]()
  val f = p.future

  val parseInt = (t: Try[String]) => t.map(Integer.parseInt)
  val intToHex = (t: Try[Int]) => t.map(Integer.toHexString)
  val strToHex = parseInt andThen intToHex

  p success "51966"

  //works in scala 2.12
//  val xformed = f.transform(strToHex, _)(ExecutionContext.global)
}

object BlockingContextBlock extends App {

  object BlockingNotAllowed extends BlockContext {
    override def blockOn[T](thunk: => T)(implicit permission: CanAwait): T =
      throw new IllegalStateException("Blocking not allowed")
  }

  def callSomeApi(): String = {
    import ExecutionContext.Implicits.global
    val f = Future("pigdog")
    Await.result(f, 10 seconds)
  }

  BlockContext.withBlockContext(BlockingNotAllowed) {
    callSomeApi()
  }

}
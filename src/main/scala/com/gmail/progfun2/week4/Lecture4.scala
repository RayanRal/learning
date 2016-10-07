package com.gmail.progfun2.week4

import java.net.URL

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by rayanral on 9/6/16.
  */
trait URLs {
  val europeUrl: URL = new URL("europe")
  val usURL: URL = new URL("USA")
}

object Lecture4 extends App with URLs {



  trait Socket extends URLs {
    def readFromMemory(): Future[Array[Byte]]
    def sendTo(url: URL, packet: Array[Byte]): Future[Array[Byte]]
  }

  class SocketImpl extends Socket {
    def readFromMemory(): Future[Array[Byte]] = ???

    def sendTo(url: URL, packet: Array[Byte]): Future[Array[Byte]] = ???

    def sendToSafe(packet: Array[Byte]): Future[Array[Byte]] =
      sendTo(europeUrl, packet) fallbackTo {
        sendTo(usURL, packet)
      } recover {
        case europeError => europeError.getMessage.toCharArray.map(_.toByte)
      }

    def retry[T](noTimes: Int)(block: => Future[T]): Future[T] = {
      if(noTimes == 0) {
        Future.failed(new Exception("sorry"))
      } else {
        block fallbackTo {
          retry(noTimes - 1){block}
        }
      }
    }
  }

//  def retryNoRec[T](noTimes: Int)(block: => Future[T]): Future[T] = {
//    val attemps = (1 to noTimes).toList.map(_ => () => block)
//    val failed = Future.failed(new Exception("boom"))
//    val result = attemps.foldRight(() => failed) ((block, a) => () => {block() fallbackTo  { a() } })
//    result
//  }

  val socket = new SocketImpl()
  val packet = socket.readFromMemory
  val confirmation: Future[Array[Byte]] = packet.flatMap(p => socket.sendTo(europeUrl, p))

}

trait FFuture[T] { self =>

  def onComplete[S](callback: Try[S] => Unit): Unit

//  def flatmap[S](f: T => FFuture[S]): FFuture[S] = new FFuture[S] {
//    def onComplete[U](callback: Try[U] => Unit): Unit =
//      self onComplete {
//        case Success(x) => f(x).onComplete(callback)
//        case Failure(e) => callback(Failure(e))
//      }
//  }
}

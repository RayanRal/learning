package com.gmail.streams

import akka.actor.{Actor, Props, ActorSystem}
import akka.stream.ActorFlowMaterializer
import akka.stream.actor.ActorSubscriber
import akka.stream.impl.ActorFlowMaterializerImpl
import scala.concurrent.ExecutionContext.Implicits.global
import akka.stream.scaladsl._

import scala.concurrent.Future

/**
 * Created by rayanral on 09/06/15.
 */
object StreamTest extends App {


  implicit val sys = ActorSystem("tokyo-sys")
  implicit val mat = ActorFlowMaterializer()

  //  val foreachSink = Sink.foreach[Int](println)
  //  val mf = Source(1 to 3).runWith(foreachSink)

//  Flow[Int].map(_.toString).runWith(Source(1 to 10), Sink.foreach(println))

  val subscriber = ActorSubscriber(sys.actorOf(Props(new Actor {
    def receive = {
      case a => println(a)
    }
  }), "parent"))

  //  Source(1 to 100).map(_.toString).filter(_.length == 2).drop(2).groupBy(_.last).runWith(subscriber)

  //                     /-f2-\
  // |in|  -f1->  |bcast|     |merge| -f3-> |out|
  //                     \-f4-/


  val f1 = Flow[Input].map(_.toIntermidiate)
  val f2 = Flow[Intermidiate].map(_.enrich)
  val f3 = Flow[Enriched].map(_.isImportant)
  val f4 = Flow[Intermidiate].mapAsync(_.enrichAsync)

  val in = Source.subscriber[Input]
  val out = Sink.publisher[Enriched]

  val bcast = Broadcast[Intermidiate]
  val merge = Merge[Enriched]

  import FlowGraphImplicits._

//  in ~> f1 ~> bcast ~> f2 ~> merge
//              bcast ~> f4 ~> merge ~> f3 -> out


  val value: Future[String] = Source("Hello world".toList)
    .map(c => c.toUpper)
    .concat(Source("!!!"))
    .runWith(Sink.fold("") {case (acc, c) => acc + c})

  value.onComplete {
    case text =>
      println(text)
      sys.terminate()
  }

}

sealed trait Input {
  def toIntermidiate = Intermidiate
}
object Input extends Input


sealed trait Intermidiate {

  def enrich = Enriched

  def enrichAsync = Future(Enriched)
}
object Intermidiate extends Intermidiate



sealed trait Enriched {
  def isImportant = true
}
object Enriched extends Enriched

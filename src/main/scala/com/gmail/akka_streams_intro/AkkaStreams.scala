package com.gmail.akka_streams_intro

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorSystem, Inbox, Props}
import akka.stream.ActorFlowMaterializer
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Flow, RunnableFlow, Sink, Source}
import com.gmail.akka_streams_intro.AkkaStreams.GenerateNumber

import scala.util.Random

/**
  * Created by rayanral on 7/14/16.
  * https://www.youtube.com/watch?v=-nmxc7DnonA
  * Introduction to Akka Streams (Dmytro Mantula, Ukraine)
  */
class RandomNumberActor extends Actor {
  override def receive: Receive = {
    case GenerateNumber(n) => sender ! Random.nextInt(n)
  }
}

object AkkaStreams extends App {

  case class GenerateNumber(i: Int)

  implicit val actorSystem = ActorSystem("ScalaCasino")

  try {
    val randomGenerator = actorSystem.actorOf(Props[RandomNumberActor], "random")
    implicit val i = Inbox.create(actorSystem)

    randomGenerator ! GenerateNumber(42)

//    val response = i.receive(1 second).asInstanceOf[Int]

  } finally {
    actorSystem.terminate()
  }

}

object App1 extends App {

  implicit val actorSystem = ActorSystem("AkkaFlow")

  implicit val mat = ActorFlowMaterializer()

  val list = List("Hello", "world")

//  Source(list).runForeach(println)

  val source = Source(1 to 10)

  val flow = Flow[Int].map{ i => s"$i * 2  = ${i * 2}"}

  val sink = Sink.foreach[String](println)

  val rg: RunnableFlow = source.via(flow).to(sink)

  rg.run()

//  actorSystem.terminate()

}

class ActorBasedSource extends Actor with ActorPublisher[Int] {
  override def receive: Receive = ???
}

object App2 extends App {

  //actor used as source - all messages, sent to actor, will be going out of that source to flow

  implicit val actorSystem = ActorSystem("AkkaFlow")
  import actorSystem.dispatcher
  implicit val mat = ActorFlowMaterializer()

  val actorRef = actorSystem.actorOf(Props[ActorBasedSource])
  val pub = ActorPublisher[Int](actorRef)

  Source(pub)




}

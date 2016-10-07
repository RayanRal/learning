package com.gmail.AkkaInAction.chapter5.ex3Messages

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem, Actor}
import akka.util.Timeout

/**
 * Created by rayanral on 17/05/15.
 */

case class Gamma(g: String)
case class Beta(b: String, g: Gamma)
case class Alpha(b1: Beta, b2: Beta)


class MyActor extends Actor {

  def receive: Receive = {
    case "Hello" =>
      println("Hi")
    case 42 =>
      println("Go ask Earth Mark II.")
    case s: String =>
      println("You sent me a string: " + s)
    case Alpha(Beta(beta1, Gamma(gamma1)), Beta(beta2, Gamma(gamma2))) =>
      println("beta1: %s, beta2: %s, gamma1: %s, gamma2: %s".format(beta1, beta2, gamma1, gamma2))
    case _ =>
      println("Huh?")
  }
}

object Asker {

  val as = ActorSystem("MyActorSystem")
  val actor = as.actorOf(Props[MyActor])
  implicit val timeout: akka.util.Timeout = Timeout(100l, TimeUnit.MILLISECONDS)

  def main(args: Array[String]): Unit = {
    import akka.pattern.ask
    actor ? 42
    println("Asked")
  }
}

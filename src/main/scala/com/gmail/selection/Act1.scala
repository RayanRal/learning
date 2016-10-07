package com.gmail.selection

import akka.actor.{Props, ActorSystem, Actor}

/**
 * Created by rayanral on 10/22/15.
 */
object ActSelectMain extends App {

  val system = ActorSystem("system")
  system.actorOf(Props[Act1], "act1")
  system.actorSelection("user/act") ! "Hello"

}

class Act1 extends Actor {

  def receive: Receive = {
    case m => println(s"Received: $m")
  }

}

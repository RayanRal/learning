package com.gmail.AkkaInAction.chapter5.ex1Shakespear

import akka.actor.{Props, ActorSystem, Actor}
import akka.actor.Actor.Receive

/**
 * Created by rayanral on 17/05/15.
 */
class BadShakespeareanActor extends Actor {

  override def receive: Receive = {
    case "Good morning" =>
      println("Him: Forsooth 'tis the 'morn, but mourneth for thou doest I do!")
    case "You're terrible!" =>
      println("Him: Yup")
  }

}

object BadShakespeareanMain {

  val system = ActorSystem("BadShakespearean")

  val actor = system.actorOf(Props[BadShakespeareanActor])

  def send(msg: String): Unit = {
    println("Me: " + msg)
    actor ! msg
    Thread.sleep(100)
  }


  def main(args: Array[String]): Unit = {
    send("Good morning")
    send("You're terrible!")
    system.terminate()
  }

}

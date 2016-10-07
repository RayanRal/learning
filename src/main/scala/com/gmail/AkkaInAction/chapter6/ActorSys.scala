package com.gmail.AkkaInAction.chapter6

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}

import scala.util.Random

/**
 * Created by rayanral on 23/05/15.
 */
class ActorSys(name: String) extends TestKit(ActorSystem(name))
                                      with ImplicitSender
                                      with DelayedInit {

  def this()  = this(s"TestSystem${Random.nextInt(5)}")

  def shutdown(): Unit = system.terminate()

  def delayedInit(f: => Unit): Unit = {
    try {
      f
    } finally {
      shutdown()
    }
  }

}

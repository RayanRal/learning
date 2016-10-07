package com.gmail.AkkaInAction.chapter15

import akka.agent.Agent
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 29/06/15.
 */
object AgentTryout extends App {

  implicit val timeout = Timeout(1 second)
  val duration = 5 seconds
  val counter = Agent(5)
  counter send {_ + 3}
  println(Await.result(counter.future(), duration))


  val secretAgent = Agent(7)
  secretAgent.sendOff{ i => Thread.sleep(10000); 5} //this executes first, but on separate from agent's thread
  secretAgent.send { 10 } //this executes when sendOff finishes
  println(Await.result(secretAgent.future(), duration)) //10

}

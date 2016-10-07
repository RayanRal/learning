package com.gmail.AkkaInAction.chapter17.pat7

import akka.actor.ActorRef
import com.gmail.AkkaInAction.chapter17.pat7.HighWaterMarkClientFSM.FailureToSend

/**
 * Created by rayanral on 03/07/15.
 */
class Client(serverActor: ActorRef) extends HighWaterMarkClient(serverActor) {

  var pingSends = 0

  override def preStart() = server ! "Ping"

  def sendPing(): Unit = {
    pingSends += 1
    if(pingSends < 3) server ! "Ping"
  }

  def messageProcessor = {
    case "Pong" =>
      sendPing()
    case FailureToSend(msg) =>
      println("Ping send failed")
      context.stop(self)
  }

  override def handleAlreadyProcessed(num: Int, hwm: Int, msg: Any): Unit = {
    sendPing()
  }

}

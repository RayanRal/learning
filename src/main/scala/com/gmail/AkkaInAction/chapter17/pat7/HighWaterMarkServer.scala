package com.gmail.AkkaInAction.chapter17.pat7

import akka.actor.{Actor, ActorRef}
import com.gmail.AkkaInAction.chapter17.pat7.HighWaterMark.HWM

/**
 * Created by rayanral on 02/07/15.
 */
object HighWaterMarkServer {

  class HWMHolder(var hwm: HWM)

  class ClientWrapper(client: ActorRef) {
    def !(message: Any)(implicit sender: ActorRef, hWMHolder: HWMHolder): Unit = {
      client ! message
      hWMHolder.hwm += 1
    }
  }

}


trait HighWaterMarkServer extends Actor {

  import HighWaterMark._
  import HighWaterMarkServer._

  implicit val watermark = new HWMHolder(HWM(0))

  var client = new ClientWrapper(context.system.deadLetters)

  def messageProcessor: Receive

  final def receive = {
    case WaterMarkedMessage(num, msg) =>
      if(num < watermark.hwm)
        sender ! MessageAlreadyProcessed(num, watermark.hwm, msg)
      else if (num > watermark.hwm)
        sender ! MessageHasBeenMissed(num, watermark.hwm, msg)
      else {
        client = new ClientWrapper(sender)
        messageProcessor(msg)
      }
    case msg =>
      client = new ClientWrapper(sender)
      messageProcessor(msg)
  }

}

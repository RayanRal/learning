package com.gmail.AkkaInAction.chapter17.pat7

import akka.actor.{Props, Actor, ActorLogging, ActorRef}

import scala.concurrent.duration._

/**
 * Created by rayanral on 03/07/15.
 */
object HighWaterMarkClient {
  import HighWaterMark.{HWM, WaterMarkedMessage}

  class ServerWrapper(server: ActorRef) {
    def !(message: Any)(implicit sender: ActorRef, hwm: HWM): Unit = {
      server ! WaterMarkedMessage(hwm, message)
    }
  }

}

abstract class NewHighWaterMarkClient(serverActor: ActorRef, retryInterval: FiniteDuration = 5 seconds, retryLimit: Int = 5)
                extends Actor with ActorLogging {

  import HighWaterMarkClientFSM._
  import HighWaterMark._

  val server = context.actorOf(Props(new HighWaterMarkClientFSM(self, serverActor, retryInterval, retryLimit)))

  def messageProcessor: Receive

  def handleAlreadyProcessed(num: Int, hwm: Int, msg: Any): Unit = log.info("Dumping response message already processed {}, {}, {}", num, hwm, msg)

  def handleMissedMessage(num: Int, hwm: Int, msg: Any): Unit = {
    log.info("Dumping response message skipped {}, {}, {}", num, hwm, msg)
    context.stop(self)
  }

  final def receive = {
    case MessageAlreadyProcessed(num, hwm, msg) => handleAlreadyProcessed(num, hwm, msg)
    case MessageHasBeenMissed(num, hwm, msg) => handleMissedMessage(num, hwm, msg)
    case m => messageProcessor(m)
  }
}

abstract class HighWaterMarkClient(serverActor: ActorRef) extends Actor with ActorLogging {

  import HighWaterMark._
  import HighWaterMarkClient._
  import language.implicitConversions

  implicit var watermark = HWM(0)

  val server = new ServerWrapper(serverActor)

  def messageProcessor: Receive

  def handleAlreadyProcessed(num: Int, hwm: Int, msg: Any): Unit = log.info("Dumping response message already processed {}, {}, {}", num, hwm, msg)

  def handleMissedMessage(num: Int, hwm: Int, msg: Any): Unit = {
    log.info("Dumping response message skipped {}, {}, {}", num, hwm, msg)
    context.stop(self)
  }

  final def receive = {
    case MessageAlreadyProcessed(num, hwm, msg) => handleAlreadyProcessed(num, hwm, msg)
    case MessageHasBeenMissed(num, hwm, msg) => handleMissedMessage(num, hwm, msg)
    case m if sender == serverActor =>
      messageProcessor(m)
      watermark += 1
    case m =>
      messageProcessor(m)
  }

}
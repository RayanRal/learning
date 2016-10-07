package com.gmail.AkkaInAction.chapter17.pat7

import akka.actor.{FSM, Actor, ActorRef}

import scala.concurrent.duration._

/**
 * Created by rayanral on 02/07/15.
 */
object HighWaterMarkClientFSM {

  sealed trait State
  case object WaitingForRequest extends State
  case object WaitingForResponse extends State

  sealed trait Data
  case object Init extends Data
  case class NoPendingRequest(hwm: Int) extends Data
  case class PendingRequest(hwm: Int, msg: Any, retries: Int) extends Data


  case class FailureToSend(msg: Any)
  case class OneRequestAtATime(pendingMsg: Any, yourMsg: Any)

}

class HighWaterMarkClientFSM(client: ActorRef, server: ActorRef, retryInterval: FiniteDuration = 5 seconds, retryLimit: Int = 5)
        extends Actor with FSM[HighWaterMarkClientFSM.State, HighWaterMarkClientFSM.Data] {

  import HighWaterMarkClientFSM._
  import HighWaterMark._

  case object RetrySend

  startWith(WaitingForRequest, NoPendingRequest(0))

  when(WaitingForRequest) {
    case Event(m: MessageAlreadyProcessed, _) =>
      stay()
    case  Event(request, NoPendingRequest(hwm)) if sender() == client =>
      server ! WaterMarkedMessage(hwm, request)
      goto(WaitingForResponse) using PendingRequest(hwm, request, 0)
  }

  onTransition {
    case WaitingForRequest -> WaitingForResponse =>
      setTimer("retry", RetrySend, retryInterval, repeat = true)
    case WaitingForResponse -> WaitingForRequest =>
      cancelTimer("retry")
  }

  when(WaitingForResponse) {
    case Event(RetrySend, PendingRequest(_, msg, `retryLimit`)) => //exceeded retry limit
      client ! FailureToSend(msg)
      stop()
    case Event(RetrySend, PendingRequest(hwm, msg, retries)) => //still can try to re-send
      server ! WaterMarkedMessage(hwm, msg)
      stay using PendingRequest(hwm, msg, retries + 1)
    case Event(m @ MessageAlreadyProcessed(num, hwm, _), _) => //request was already processed
      client ! m
      goto(WaitingForRequest) using NoPendingRequest(hwm)
    case Event(m @ MessageHasBeenMissed(num, hwm, _), _) => //we missed a message, it's a critical failure - so we stop
      client ! m
      stop()
    case Event(response, PendingRequest(hwm, _, _)) if sender == server => //message was successfully processed, sending it back and waiting for new
      client ! response
      goto(WaitingForRequest) using NoPendingRequest(hwm + 1)
    case Event(request, PendingRequest(_, msg, _)) if sender == client => //client made another call when previous is not yet processed
      client ! OneRequestAtATime(msg, request)
      stay
  }

}
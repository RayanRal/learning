package com.gmail.AkkaInAction.chapter15

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, FSM, ActorRef}
import akka.agent.Agent

import scala.collection.immutable.Queue
import scala.concurrent.duration.Duration

/**
 * Created by rayanral on 29/06/15.
 */
object Bathroom {

  sealed trait State
  case object Vacant extends State
  case object Occupied extends State

  sealed trait Data
  case class InUse(by: ActorRef, atTimeMillis: Long, queue: Queue[ActorRef]) extends Data
  case object NotInUse extends Data

  case object IWannaUseTheBathroom
  case object YouCanUseTheBathroomNow
  case class Finished(gender: Gender)

  private def updateCounter(male: Agent[GenderAndTime], female: Agent[GenderAndTime], gender: Gender, dur: Duration): Unit = {
    gender match {
      case Male => male send {c =>
        GenderAndTime(Male, dur.max(c.peakDuration), c.count + 1)
      }
      case Female => female send {c =>
        GenderAndTime(Female, dur.max(c.peakDuration), c.count + 1)
      }
    }
  }
}

class Bathroom (femaleCounter: Agent[GenderAndTime], maleCounter: Agent[GenderAndTime]) extends Actor with FSM[Bathroom.State, Bathroom.Data] {
  import Bathroom._

  startWith(Vacant, NotInUse)

  when(Vacant) {
    case Event(IWannaUseTheBathroom, _) =>
      sender ! YouCanUseTheBathroomNow
      goto(Occupied) using InUse(by = sender(), atTimeMillis = System.currentTimeMillis(), queue = Queue())
  }

  when(Occupied) {
    case Event(IWannaUseTheBathroom, data: InUse) =>
      stay() using data.copy(queue = data.queue.enqueue(sender()))
    case Event(Finished(gender), data: InUse) if sender() == data.by =>
      updateCounter(maleCounter, femaleCounter, gender, Duration(System.currentTimeMillis() - data.atTimeMillis, TimeUnit.MILLISECONDS))
      if(data.queue.isEmpty)
        goto(Vacant) using NotInUse
      else {
        val (next, q) = data.queue.dequeue
        next ! YouCanUseTheBathroomNow
        stay() using InUse(next, System.currentTimeMillis, q)
      }
  }
}

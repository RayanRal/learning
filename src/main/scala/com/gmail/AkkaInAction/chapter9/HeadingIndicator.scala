package com.gmail.AkkaInAction.chapter9

import akka.actor.{Actor, ActorLogging}
import com.gmail.AkkaInAction.chapter6.EventSource
import com.gmail.AkkaInAction.chapter9.HeadingIndicator.{HeadingUpdate, BankChange}
import scala.concurrent.duration._

/**
 * Created by rayanral on 14/06/15.
 */
object HeadingIndicator {

  // Indicates that something has changed how fast we're changing direction
  case class BankChange(amount: Float)
  // The event published by the HeadingIndicator to listeners that want to know where we're headed
  case class HeadingUpdate(heading: Float)

}

trait HeadingIndicator extends Actor with ActorLogging {
  this: EventSource =>
  import scala.concurrent.ExecutionContext.Implicits.global

  case object Tick

  val maxDegreesPerSecond = 5

  val ticker =  context.system.scheduler.schedule(100.millis, 100.millis, self, Tick)

  var lastTick: Long = System.currentTimeMillis()

  var rateOfBank = 0f

  var heading = 0f

  def headingIndicatorReceive: Receive = {
    case BankChange(amount) =>
      rateOfBank = amount.min(1.0f).max(-1.0f)
    case Tick =>
      val tick = System.currentTimeMillis()
      val timeDelta = (tick - lastTick) / 1000f
      val degs = rateOfBank * maxDegreesPerSecond
      heading = (heading + (360 + (timeDelta * degs))) % 360
      lastTick = tick

      // Send the HeadingUpdate event to our listeners
     sendEvent(HeadingUpdate(heading))
  }

  def receive = eventSourceReceive orElse headingIndicatorReceive

  override def postStop(): Unit = ticker.cancel()

}
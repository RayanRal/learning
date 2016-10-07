package com.gmail.AkkaInAction.chapter17.pat1

/**
 * Created by rayanral on 01/07/15.
 */
trait MiddleHandler { this: ReceiveCompositingActor =>

  val SmallTalkReceiver1 = 15
  val SmallTalkReceiver2 = 16

  def smallTalkHandler1: Receive = {
    case "So how's the weather?" => sender ! "Rainy, and lousy..."
  }


  def smallTalkHandler2: Receive = {
    case "How about the kids?" => sender ! "Sure"
  }

  receivePartials += (SmallTalkReceiver1 -> smallTalkHandler1)
  receivePartials += (SmallTalkReceiver2 -> smallTalkHandler2)

}

package com.gmail.AkkaInAction.chapter17.pat1

/**
 * Created by rayanral on 01/07/15.
 */
class ComposedActor extends ReceiveCompositingActor
                    with HelloHandler with GoodbyeHandler with MiddleHandler {

  val MoodReceiver = 50

  def alternateSmallTalk1: Receive = {
    case "So how's the weather?" => sender ! "Sunny! Amazing!"
  }

  def alternateSmallTalk2: Receive = {
    case "How about the kids?" => sender ! "Funny! Smart! Awesome!"
  }

  def moodHandler: Receive = {

    case "Happy" =>
      becomeNew(SmallTalkReceiver1, alternateSmallTalk1)
      becomeNew(SmallTalkReceiver2, alternateSmallTalk2)

    case "Grumpy" =>
      becomeNew(SmallTalkReceiver1, smallTalkHandler1)
      becomeNew(SmallTalkReceiver2, smallTalkHandler2)
  }

  receivePartials += (MoodReceiver -> moodHandler)

}

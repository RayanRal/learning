package com.gmail.AkkaInAction.chapter17.pat1

/**
 * Created by rayanral on 01/07/15.
 */
trait GoodbyeHandler { this: ReceiveCompositingActor =>

  val GoodbyeReceiver = 50

  def goodbyeHandler: Receive = {
    case "Goodbye" => sender ! "So long"
  }

  receivePartials += (GoodbyeReceiver -> goodbyeHandler)

}

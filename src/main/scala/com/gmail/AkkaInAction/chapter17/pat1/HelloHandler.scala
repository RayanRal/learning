package com.gmail.AkkaInAction.chapter17.pat1

/**
 * Created by rayanral on 01/07/15.
 */
trait HelloHandler { this: ReceiveCompositingActor =>

  val HelloReceiver = 10

  def helloHandler: Receive = {
    case "Hello" => sender ! "Hithere!"
  }

  receivePartials += (HelloReceiver -> helloHandler)


}

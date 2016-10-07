package com.gmail.AkkaInAction.chapter17.pat7

/**
 * Created by rayanral on 03/07/15.
 */
class Server extends HighWaterMarkServer {

  def messageProcessor = {
    case "Ping" =>
      client !  "Pong"
  }

}

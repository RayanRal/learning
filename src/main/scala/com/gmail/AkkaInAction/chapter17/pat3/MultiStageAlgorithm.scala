package com.gmail.AkkaInAction.chapter17.pat3

import akka.actor.{Actor, ActorRef}
import akka.util.Timeout
import com.gmail.AkkaInAction.chapter17.pat3.MultiStageAlgorithm._
import scala.concurrent.duration._
import akka.pattern.{ask, pipe}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 01/07/15.
 */
object MultiStageAlgorithm {

  case class UserAppData(username: String, appData: List[Map[String, String]])

  case class CollectionFailed(error: String)

  case class GetAppList(username: String)
  case class AppList(appList: List[String])

  case class GetAppData(app: String)
  case class AppData(info: Map[String, String])
}


class MultiStageAlgorithm(username: String, dataStore: ActorRef, returnTo: ActorRef) extends Actor {

  implicit val askTimeout = Timeout(5 seconds)

  case class CollectData(appList: List[String], appDetails: List[Map[String, String]])

  override def preStart(): Unit = {
    dataStore ! GetAppList(username)
  }

  def receive = {
    case AppList(appList) =>
      self ! CollectData(appList, List.empty)
    case CollectData(Nil, appData) => //we're finished
      returnTo ! UserAppData(username, appData)
      context.stop(self)
    case CollectData(leftToCheck, appData) => //continuing to work
      dataStore ? GetAppData(leftToCheck.head) map {
        case AppData(propertyMap) =>
          CollectData(leftToCheck.tail, propertyMap :: appData) //send to ourselves what we already know, and what we've left to gather
      } recover {
        case e => CollectionFailed(e.toString)
      } pipeTo self
  }

}
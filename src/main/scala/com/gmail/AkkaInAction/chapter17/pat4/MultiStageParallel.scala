package com.gmail.AkkaInAction.chapter17.pat4

import akka.actor.{Props, ActorRef, Actor}
import akka.util.Timeout
import com.gmail.AkkaInAction.chapter17.pat4.MultiStageParallel.{AppList, GetAppList}
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 02/07/15.
 */

object MultiStageParallel {

  case class UserAppData(username: String, appData: List[Map[String, String]])

  case class CollectionFailed(error: String)

  case class GetAppList(username: String)
  case class AppList(appList: List[String])

  case class GetAppData(app: String)
  case class AppData(info: Map[String, String])
}

class AppDataStore extends Actor {
  def receive = ???
}

class MultiStageParallel(username: String, dataStore: ActorRef, returnTo: ActorRef) extends Actor {

  import MultiStageParallel._

  implicit val askTimeout = Timeout(5 seconds)

  val ds = context.actorOf(Props[AppDataStore])

  val results = (ds ? GetAppList(username)).mapTo[AppList].flatMap { applist =>
    val AppList(list) = applist

    Future.sequence(list.map {appName =>
      (ds ? GetAppData(appName)).mapTo[AppData].map {data =>
        val AppData(propertyMap) = data
        propertyMap
      }
    })
  }

  def receive = ???

}

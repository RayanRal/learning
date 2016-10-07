package com.gmail.cluster

import akka.actor.{Props, ActorSystem, Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

/**
 * Created by rayanral on 04/07/15.
 */
class SimpleClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) =>
      log.info(s"Member is up: ${member.address}")
    case UnreachableMember(member) =>
      log.info(s"Member detected unreachable: $member")
    case MemberRemoved(member, status) =>
      log.info(s"Member removed: $member")
    case _: MemberEvent =>
      //ignore
  }

}


object AkkaClusterExample extends App {
  val system = ActorSystem("ClusterSystem")
  system.actorOf(Props[SimpleClusterListener], "clusterListener")
  system.awaitTermination()
}

//[ERROR] [07/04/2015 14:29:46.181] [system-akka.remote.default-remote-dispatcher-17]
// [akka.tcp://system@127.0.0.1:2551/system/endpointManager/reliableEndpointWriter-akka.tcp%3A%2F%2Fsystem%40127.0.0.1%3A2551-2/endpointWriter]
// dropping message [class akka.actor.ActorSelectionMessage] for non-local recipient [Actor[akka.tcp://ClusterSystem@127.0.0.1:2551/]]
// arriving at [akka.tcp://ClusterSystem@127.0.0.1:2551]
// inbound addresses are [akka.tcp://system@127.0.0.1:2551]

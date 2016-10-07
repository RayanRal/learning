package com.gmail.wikidata.streams

import java.io.{FileInputStream, File}
import java.util.zip.GZIPInputStream

import akka.actor.ActorSystem
import akka.stream.{ActorFlowMaterializer, FlattenStrategy}
import akka.stream.FlattenStrategy.Concat
import akka.stream.scaladsl._
import scala.concurrent.{Future, ExecutionContext}
import scala.io.{Source => ioSource}
import scala.xml.Attribute
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by rayanral on 11/07/15.
 */
object FlowElements {

  def source(file: File): Source[String] = {
    val compressed = new GZIPInputStream(new FileInputStream(file), 65536)
    val source = ioSource.fromInputStream(compressed, "utf-8")
    Source(() => source.getLines()).drop(1)
  }

  def parseJson(langs: Seq[String])(implicit ec: ExecutionContext): Flow[String, WikidataElement] = {
    Flow[String].mapAsyncUnordered(s => Future(parseItem(langs, s))).collect {
      case Some(v) => v
    }
  }

  def parseItem(langs: Seq[String], line: String): Option[WikidataElement] = {
    Some(WikidataElement(line.split(" ")(1), Map("1" -> line.split(" ")(2)))) //just doing something
  }

  def logEveryNSink[T](n: Int) = Sink.fold(0) {(counter, elem: T) =>
    if(counter % n == 0) println(s"Processing element $counter: $elem")
    counter + 1
  }

  def checkSameTitles(langs: Set[String]): Flow[WikidataElement, Boolean] =
    Flow[WikidataElement].filter(_.sites.keySet == langs).map { x =>
      val titles = x.sites.values
      titles.forall( _ == titles.head)
    }

//  def count = Sink.fold((0,0)) {
//    case ((t, f), true) => (t+1, f)
//    case ((t, f), false) => (t, f+1)
//  }

}


//id is the wikidata canonical id
//sites is a map of titles indexed by language
case class WikidataElement(id: String, sites: Map[String, String])

object FlowCreation extends App {

  implicit val sys = ActorSystem("wikidata-processor")
  implicit val mat = ActorFlowMaterializer()

  val input = new File("someValidPath")


  import FlowGraphImplicits._

  val source = FlowElements.source(input)
  val parser = FlowElements.parseJson(List("en"))
  val logger = FlowElements.logEveryNSink[WikidataElement](5)
  val checkSameTitles = FlowElements.checkSameTitles(Set("en"))



  val graph = FlowGraph {implicit b =>
      val broadcast = Broadcast[WikidataElement](OperationAttributes.name(""))

      source ~> parser ~>  broadcast ~> logger
                           broadcast ~> checkSameTitles
  }
}
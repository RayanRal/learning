package com.gmail.AkkaInAction.chapter12

import java.util.NoSuchElementException
import java.util.concurrent.Executors

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._

import scala.concurrent.{Await, Promise, Future, ExecutionContext}
import scala.util.{Success, Failure}

/**
 * Created by rayanral on 25/06/15.
 */
object FutureEasy extends App {

  val execService = Executors.newCachedThreadPool()
  implicit val executionContext = ExecutionContext.fromExecutor(execService)
  val duration = 1 second
//  val futureFib = Future{ 5 }

  val promise = Promise[String]()
  val future = promise.future
  promise.success("Promise keeped")
  println(future.value)

  val res = Future {
    5
  } map {i =>
    11
  }
  res.onComplete(println)

  val res2 = for {
    i <- Future(5)
    j <- Future(11)
  } yield j



  Future{"5"}.transform(s => s.toInt, t => new Exception(t))
  val newOops = Future{5}.filter(_ % 2 == 0).fallbackTo(Future{3})

  val newOopsResult = Await.result(newOops, duration)

//  val oops = Future { 5 }.filter(_ % 2 == 0)
//  oops.recover {
//    case e: NoSuchElementException => "Will be this"
//    case e: ArithmeticException => "Not gonna happen"
//    case _ => "Some other"
//  }

//  Await.result(oops, duration)

  class CacheTimeOutException extends Exception

  val longCalculation = Future { 5 }
  val result = Future {
    throw new CacheTimeOutException
  }.recoverWith {
    case e: CacheTimeOutException => longCalculation
  }


  case class Matrix(rows: Int, columns: Int) {
    def mult(other: Matrix) = {
      if (columns == other.rows) {
        Some(Matrix(rows, other.columns))
      } else {
        None
      }
    }
  }

  def matrixMult(matrices: Seq[Matrix]): Option[Matrix] = {
    matrices.tail.foldLeft(Option(matrices.head)) {(acc, m) =>
      acc.flatMap(a => a.mult(m))
    }
  }

  val randoms = (1 to 20000) map {_ =>
    scala.util.Random.nextInt(500)
  }

  val matrices = randoms.zip(randoms.tail).map {
    case (row, column) => Matrix(row, column)
  }

  val futs = matrices.grouped(500).map {ms =>
    Future(matrixMult(ms))
  }.toSeq

  val multResultFuture = Future.sequence(futs).map{r =>
    matrixMult(r.flatten)
  }


  def longCalc = Future {
    Thread.sleep(50)
    "5 from the calculation"
  }
  def cacheCalc = Future {
    Thread.sleep(20)
    "3 from cache"
  }
  val someRes = Future.firstCompletedOf(List(longCalc, cacheCalc)).onComplete {s =>
    println(s)
  }


  val words = Vector("Joker", "Batman", "Two Face", "Catwoman")

  val futWords = words map {w =>
    Future {
      Thread.sleep(15)
      println(s"$w finished")
      w
    }
  }

  val sum = Future.fold(futWords)(0) { (acc, word) =>
    word.foldLeft(acc) { (a, c) => a + c.toInt }
  }

  val foundFuture = Future.find(futWords){ l => l.equalsIgnoreCase("joker") }
  val found = Await.result(foundFuture, duration)
  println(found)


  //onSuccess
  Future{ 13 }.filter(_ % 2 == 0).fallbackTo(Future{"That didn't work"}).onSuccess {
    case i: Int => println(s"Bingo! $i")
    case m => println(s"Error! $m")
  }

  //onFailure
  Future{13}.filter(_ % 2 == 0).onFailure {
    case m => println(s"Error! $m")
  }

  //onComplete
  Future{scala.util.Random.nextInt(100)}.filter(_ % 2 == 0).onComplete {
    case Success(m) => println(s"Bingo $m")
    case Failure(_) => println("LeftError")
  }

  //side-effects in random order
  val f = Future{12}.filter(_ % 2 == 0)
  f.onFailure {
    case _ => println("Error!")
  }
  f.onSuccess{
    case i: Int => println(i)
  }
  f.onSuccess {
    case i: Int => println(s"$i is keen")
  }
  f.onSuccess{
    case i: Int => println(s"$i is wicked!")
  }

  implicit val timeout = Timeout(5 seconds)

  //actors
  class MutableActor(someOtherActor: ActorRef) extends Actor {
    var currentState: State = InitialState()

    def receive = {
      case Go =>
        (someOtherActor ? GetData).onSuccess {
          case data => currentState = DataReceivedState(data)
        }
    }
  }

  abstract class State()
  case class InitialState() extends State()
  case class DataReceivedState(data: Any) extends State()

  case class Go()

  case class GetData()
  case class AlteredData(i: Int)

  //piping
  import akka.pattern.pipe
  val actorSystem = ActorSystem("AcSystem")
  val actor1 = actorSystem.actorOf(Props(new Actor {
    def receive = ???
  }))
  val actor2 = actorSystem.actorOf(Props(new Actor {
    def receive = ???
  }))
  (actor1 ? GetData).mapTo[String].map {
    str => AlteredData(str.toInt)
  } pipeTo(actor2)




}

package com.gmail.fpScala.chapter8

import com.gmail.fpScala.chapter6.RNG
import com.gmail.fpScala.chapter6.RNG.State
import com.gmail.fpScala.chapter8.Prop.{FailedCase, SuccessCount}

/**
 * Created by rayanral on 26/08/15.
 */
object MainRunner extends App {




}

trait ToProp {
  def sum: List[Int] => Int
}

//case class Gen[+A](sample: State[RNG, A], exhaustive: Stream[A])

object Gen {

  type Gen[A] = State[RNG,A]

//  def unit[A](a: => A): Gen[A] = new State[RNG, A]()

//  def listOf[A](a: Gen[A]): Gen[List[A]]

//  def listOfN[A](n: Int, a: Gen[A]): Gen[List[A]]

//  def forall[A](a: Gen[A])(f: A => Boolean): Prop

//  def choose(start: Int, stopExclusive: Int): Gen[Int] =
//    Gen(State(RNG.positiveInt).run(rng: RNG => ))
}

trait Prop {

  def check: Either[FailedCase, SuccessCount]

//  def &&(p: Prop): Prop = new Prop {
//    override def check: Either[FailedCase, SuccessCount] = super.check match {
//      case Left(fc) => Left(fc)
//      case Right(sc) => p.check match {
//        case Left(fc2) => Left(fc2)
//        case Right(sc2) => Right(sc + sc2)
//      }
//    }
//  }

}

object Prop {

  type FailedCase = String

  type SuccessCount = Int

}


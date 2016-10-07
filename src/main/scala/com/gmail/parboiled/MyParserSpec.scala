package com.gmail.parboiled

import org.scalatest.{WordSpecLike, MustMatchers}

import scala.util.{Failure, Success}

/**
  * Created by rayanral on 11/24/15.
  */
class MyParserSpec extends WordSpecLike with MustMatchers{

  val p1 = new MyParser("Match")
  val p2 = new MyParser("match")
  val p3 = new MyParser("much")

  "myParser" should {
    "return something" in {
      val res1 = p1.myMainRule.run()
      res1 mustBe Success()
      val res2 = p2.myMainRule.run()
      res2 mustBe Success()
      val res3 = p3.myMainRule.run()
    }
  }
}

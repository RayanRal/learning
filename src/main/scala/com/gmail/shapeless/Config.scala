package com.gmail.shapeless

import shapeless._

/**
  * Created by rayanral on 5/6/16.
  */
case class DeviceConfig(devName: String, bool: Boolean)
case class CheckConfig(check1: Int, check2: String)
case class Config(name: String, cConfig: CheckConfig, deviceConfig: DeviceConfig)

object Flatter extends App {

  val listConf = Generic[Config]
  val realConf = Config("name", CheckConfig(1, "check2"), DeviceConfig("devName", bool = false))
  val parsed = listConf.to(realConf)

  def go(g: HList): String = g match {
    case (h: Product) :: t =>
      println(s"Head: $h")
      h.toString + "." + go(t)
    case t :: HNil =>
      t.toString
    case HNil => ""
  }

  val r = go(parsed.asInstanceOf[HList])

  println(r)

}

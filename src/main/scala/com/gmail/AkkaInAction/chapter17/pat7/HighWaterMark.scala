package com.gmail.AkkaInAction.chapter17.pat7

/**
 * Created by rayanral on 02/07/15.
 */
object HighWaterMark {

  object HWM {
    import language.implicitConversions
    def apply(i: Int) = new HWM(i)
    implicit def int2HWM(i: Int): HWM = HWM(i)
    implicit def HWM2Int(hwm: HWM): Int = hwm.hwm
  }

  class HWM(val hwm: Int) {
    def +(i: Int): HWM = HWM(hwm + i)
    override def toString = hwm.toString
  }

  case class WaterMarkedMessage(num: Int, msg: Any)

  case class MessageAlreadyProcessed(msgNum: Int, hwm: Int, msg: Any)
  case class MessageHasBeenMissed(msgNum: Int, hwm: Int, msg: Any)

}

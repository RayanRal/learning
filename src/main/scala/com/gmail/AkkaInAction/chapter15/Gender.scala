package com.gmail.AkkaInAction.chapter15

import scala.concurrent.duration.Duration

/**
 * Created by rayanral on 29/06/15.
 */
sealed abstract class Gender

case object Male extends Gender
case object Female extends Gender

case class GenderAndTime(gender: Gender, peakDuration: Duration, count: Int)

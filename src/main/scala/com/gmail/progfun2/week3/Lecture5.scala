package com.gmail.progfun2.week3

/**
  * Created by rayanral on 9/5/16.
  */
object Lecture5  extends App {

  type Action = () => Unit

  trait Simulation {
    private var curtime: Int = 0
    def currentTime: Int = curtime
    case class Event(time: Int, action: Action)
    private type Agenda = List[Event]
    private var agenda: Agenda = List.empty

    private def insert(agenda: Agenda, ev: Event): Agenda = agenda match {
      case h :: tail if h.time <= ev.time => h :: insert(tail, ev)
      case _ => ev :: agenda
    }

    def afterDelay(delay: Int)(block: => Unit): Unit = {
      val event = Event(curtime + delay, () => block)
      agenda =  insert(agenda, event)
    }

    def run(): Unit = {
      afterDelay(0) {
        println(s"Started, current time $curtime")
      }
      loop()
    }

    def probe(name: String, wire: Wire): Unit = {
      def probeAction(): Unit = {
        println(s"$name $curtime value = ${wire.getSignal}")
      }
      wire.addAction(probeAction)
    }

    private def loop(): Unit = agenda match {
      case first :: rest =>
        agenda = rest
        curtime = first.time
        first.action()
        loop()
      case Nil =>
    }
  }

  trait Parameters {
    def InverterDelay = 2
    def AndDelay = 3
    def OrDelay = 5
  }

  class Wire {
    private var sigVal = false
    private var actions: List[Action] = List.empty

    def getSignal: Boolean = sigVal
    def setSignal(sig: Boolean): Unit = if(sig != sigVal) {
      sigVal = sig
      actions.foreach(_())
    }
    def addAction(a: Action): Unit = {
      actions = a :: actions
      a()
    }
  }

  def inverter(input: Wire, output: Wire): Unit = {
    def invertAction(): Unit = {
      val inputSig = input.getSignal
//      afterDelay
    }
  }

}

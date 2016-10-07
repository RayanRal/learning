package com.gmail.AkkaInAction.chapter17.pat5

import akka.event.{LookupClassification, ActorEventBus}

/**
 * Created by rayanral on 02/07/15.
 */
sealed class DefaultsTo[A, B]


trait LowPriorityDefaultsTo {
  implicit def overrideDefault[A, B] = new DefaultsTo[A, B]
}


object DefaultsTo extends LowPriorityDefaultsTo {
  implicit def default[B] = new DefaultsTo[B, B]
}


object EventBusForActors {
  val classify: Any => Class[_] = {event => event.getClass}
}


class EventBusForActors[EventType, ClassifierType](classifier: EventType => ClassifierType = EventBusForActors.classify)
                                                  (implicit e: EventType DefaultsTo Any, c: ClassifierType DefaultsTo Class[_]) extends ActorEventBus with LookupClassification {

  type Event = EventType

  type Classifier = ClassifierType

  protected def classify(event: Event): Classifier = classifier(event)

  protected def mapSize(): Int = 32

  protected def publish(event: Event, subscriber: Subscriber): Unit = subscriber ! event

}
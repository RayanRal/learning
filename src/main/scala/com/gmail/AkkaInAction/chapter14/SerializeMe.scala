package com.gmail.AkkaInAction.chapter14

import java.io.NotSerializableException

import akka.actor.ActorSystem
import akka.serialization.SerializationExtension
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}

/**
 * Created by rayanral on 29/06/15.
 */
class SerializeMe(val message: String) {

  override def equals(a: Any): Boolean = {
    if(!a.isInstanceOf[SerializeMe]) false
    else a.asInstanceOf[SerializeMe].message == message
  }

}

class SerializeMeSerializer extends akka.serialization.Serializer {

  override def includeManifest: Boolean = false

  override def identifier: Int = 42

  override def toBinary(o: AnyRef): Array[Byte] = {
    if(!o.isInstanceOf[SerializeMe]) {
      throw new NotSerializableException(s"SerializeMeSerializer can't serialize ${o.getClass.getName}")
    }
    val m = o.asInstanceOf[SerializeMe]
    m.message.getBytes("UTF-8")
  }

  override def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = {
    new SerializeMe(new String(bytes, "UTF-8"))
  }
}

class SerializerTest extends TestKit(ActorSystem("SerializationSpec"))
                        with ImplicitSender
                        with WordSpecLike
                        with MustMatchers {
  val serialization = SerializationExtension(system)

  "Serializer" should {
    "serialize" in {
      val original = new SerializeMe("Hello world!")
      val serializer = serialization.findSerializerFor(original)
      val bytes = serializer.toBinary(original)
      val copy = serializer.fromBinary(bytes)
      original mustBe copy
    }
  }
}
package com.gmail.patterns.cake

/**
  * Created by rayanral on 11/17/15.
  */
trait NameProviderComponent {

  val nameProvider: NameProvider

  trait NameProvider {
    def getName: String
  }
}

trait NameProviderComponentImpl extends NameProviderComponent {

  class NameProviderImpl extends NameProvider {
    def getName = "world"
  }
}


trait SayHelloComponent {

  val sayHelloService: SayHelloService

  trait SayHelloService {
    def sayHello(): Unit
  }

}

trait SayHelloComponentImpl extends SayHelloComponent {

  this: NameProviderComponent =>

  class SayHelloServiceImpl extends SayHelloService {
    def sayHello() = println(s"Hello ${nameProvider.getName}")
  }
}


object ComponentRegistry extends SayHelloComponentImpl with NameProviderComponentImpl {

  val nameProvider = new NameProviderImpl
  val sayHelloService = new SayHelloServiceImpl

}
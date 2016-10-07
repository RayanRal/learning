package com.gmail.patterns.cake

/**
  * Created by rayanral on 11/19/15.
  */
case class User(name: String)

case class EntityManager()

trait UserRepositoryComponent {

  def userLocator: UserLocator
  def userUpdater: UserUpdater

  trait UserLocator {
    def findAll: List[User]
  }

  trait UserUpdater {
    def save(user: User)
  }

}


trait UserRepositoryJpaComponent extends UserRepositoryComponent {

  val em: EntityManager

  def userLocator = new UserLocatorJpa(em)
  def userUpdater = new UserUpdaterJpa(em)

  class UserLocatorJpa(em: EntityManager) extends UserLocator {
    def findAll: List[User] = List.empty
  }

  class UserUpdaterJpa(em: EntityManager) extends UserUpdater {
    def save(user: User) = println(s"saved ${user.name}")
  }

}


trait UserServiceComponent {
  def userService: UserService

  trait UserService {
    def findAll: List[User]
    def save(user: User)
  }

}

trait DefaultUserServiceComponent extends UserServiceComponent {
  this: UserRepositoryComponent =>

  def userService = new DefaultUserService

  class DefaultUserService extends UserService {
    def findAll = userLocator.findAll
    def save(user: User) = userUpdater.save(user)
  }

}


object ApplicationLive {
  val userServiceComponent = new DefaultUserServiceComponent with UserRepositoryJpaComponent {
    val em = EntityManager()
  }

  val userService = userServiceComponent.userService
}
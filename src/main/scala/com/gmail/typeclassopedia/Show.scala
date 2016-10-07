package com.gmail.typeclassopedia

/**
 * Created by rayanral on 22/08/15.
 */
//typeclass
trait Show[A] {
  def shows(a: A): String
}


//ex: Turn a list of strings into a list of ints
/*trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}*/

//(parse("3") <*> parse("Nope"))(_ + _)
/*trait Applicative[F[_]] extends Functor {

  def <*>[A, B](fa: F[A])(f: F[A => B]): F[B]

  //lift pure constant to applicative
  def point[A](a: A): F[A]

}*/

//params.get("x") >>= parse //Option[Int]
/*trait Monad[F[_]] extends Applicative {
  def >>=[A, B](fa: F[A])(f: A => F[B]): F[B] //bind
}*/

//def params.get(): Option[String]

object Something extends App {

  //particular type belongs to typeclass
  implicit val IntShow = new Show[Int] {
    def shows(a: Int) = a.toString
  }

  def shows[A : Show](a: A) = implicitly[Show[A]].shows(a)

  /*implicit val OptionFunctor = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
      case Some(a) => Some(f(a))
      case None => None
    }
  }*/

//  def parse(String): Option[Int]

}


package com.gmail.progfun2.week1

/**
  * Created by rayanral on 8/22/16.
  */
object Lecture3  extends App {

  case class Book(title: String, authors: List[String])

  val books: List[Book] =
    List(
      Book(title = "Effective scala", authors = List("1", "2")),
      Book(title = "Puzzlers", authors = List("Odersky"))
    )

  val l = for {
    b1 <- books
    b2 <- books
    if b1.title < b2.title
    a1 <- b1.authors
    a2 <- b2.authors
    if a1 == a2
  } yield a1
  println(s"!! $l")


  for (
    b <- books;
    a <- b.authors if a.startsWith("Bird")
  ) yield b.title

  books.withFilter(b => b.authors.startsWith("Bird")).map(_.title)

}

trait Generator[+T] {
  self => //alias of this

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    def generate: S = f(self.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate: S = f(self.generate).generate
  }
}

sealed trait Tree
case class Inner(left: Tree, right: Tree) extends Tree
case class Leaf(x: Int) extends Tree

  object RandomGens {
    val integers = new Generator[Int] {
      val rand = new java.util.Random()
      def generate: Int = rand.nextInt()
    }

    def booleans = new Generator[Boolean] {
      def generate: Boolean = integers.generate > 0
    }

    def single[T](x: T) = new Generator[T] {
      def generate: T = x
    }


    //LISTS
    def lists: Generator[List[Int]] = for {
      isEmpty <- booleans
      list <- if(isEmpty) emptyList else nonEmptyList
    } yield list

    def emptyList = single(Nil)

    def nonEmptyList = for {
      head <- integers
      tail <- lists
    } yield head :: tail



    //TREES
    def trees: Generator[Tree] = for {
      isLeaf <- booleans
      tree <- if(isLeaf) leaf else inner
    } yield tree

    def leaf: Generator[Leaf] = new Generator[Leaf] {
      def generate: Leaf = Leaf(integers.generate)
    }

    def inner: Generator[Inner] = for {
      l <- trees
      r <- trees
    } yield Inner(l, r)

  }

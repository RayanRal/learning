package com.gmail.shapeless

/**
  * Created by rayanral on 11/22/15.
  *
  * http://www.cakesolutions.net/teamblogs/solving-problems-in-a-generic-way-using-shapeless
  */
case class Coordinate(x: Int, y: Int)

sealed trait Shape
case class Circle(radius: Int, center: Coordinate) extends Shape
case class Rectangle(corner1: Coordinate, corner2: Coordinate) extends Shape
case class Triangle(corner1: Coordinate, corner2: Coordinate, corner3: Coordinate) extends Shape

case class Surface(name: String, shape1: Shape, shape2: Shape)



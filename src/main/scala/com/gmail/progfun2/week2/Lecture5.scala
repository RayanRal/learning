package com.gmail.progfun2.week2

/**
  * Created by rayanral on 8/30/16.
  */
object Lecture5 extends App {

  val problem = new Pouring(Vector(4, 7))
  problem.moves

}

class Pouring(capacity: Vector[Int]) {

  //states
  type State = Vector[Int]
  val initialState: Vector[Int] = capacity map (x => 0)

  //moves
  trait Move {
    def change(state: State): State
  }

  case class Empty(glass: Int) extends Move {
    def change(state: State): State = state updated(glass, 0)
  }
  case class Fill(glass: Int) extends Move {
    def change(state: State): State = state updated(glass, capacity(glass))
  }
  case class Pour(from: Int, to: Int) extends Move {
    def change(state: State): State = {
      val amount = state(from) min (capacity(to) - state(to))
      state updated(from, state(from) - amount) updated (to, state(to) + amount)
    }
  }

  val glasses = 0 to capacity.length

  val moves =
    (for (g <- glasses) yield Empty(g)) ++
    (for (g <- glasses) yield Fill(g)) ++
    (for (from <- glasses; to <- glasses if from != to) yield Pour(from, to))


  //paths
  class Path(history: List[Move], val endState: State) {
//    def endState: State = history.foldRight(initialState)((move, state) => move.change(state))//trackState(history)

    def extend(move: Move) = new Path(move :: history, move change endState)

    override def toString: String = history.reverse.mkString(" ") + s" --> $endState"

    //replaced with foldRight
    private def trackState(xs: List[Move]): State = xs match {
      case head :: tail => head.change(trackState(tail))
      case Nil => initialState
    }
  }

  val initialPath = new Path(Nil, initialState)

  def from(paths: Set[Path], explored: Set[State]): Stream[Set[Path]] =
    if (paths.isEmpty) Stream.empty
    else {
      val more = for {
        path <- paths
        next <- moves map path.extend
        if !explored.contains(next.endState)
      } yield next
      paths #:: from(more, explored ++ (more map (_.endState)))
    }

  val pathSets = from(Set(initialPath), Set(initialState))

  def solution(target: Int): Stream[Path] =
    for (
      pathSet <- pathSets;
      path <- pathSet
      if path.endState.contains(target)
    ) yield path
}

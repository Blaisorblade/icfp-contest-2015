package astarprime
package test

import org.scalatest._
import org.scalatest.matchers._


class AStarPrimeTest extends FunSpec with Matchers {

  // x, y, rotation (0, 1, 2, 3)
  type State = (Int, Int, Int)

  trait Command
  case object Left extends Command
  case object Right extends Command
  case object Up extends Command
  case object Down extends Command
  case object RotateLeft extends Command
  case object RotateRight extends Command

  // Map: 5 x 8
  val width  = 5
  val height = 8
  val blocked = List((0, 1), (1, 1), (2, 1), (3, 1))

  val engine = new astarprime.Engine[State, Command] {
    def valid(self: State): Boolean = self match {
      case (x, y, r) =>
        x >= 0 && x < width &&
        y >= 0 && y < height && !(blocked contains (x, y))
    }
    def bisimilar(self: State, other: State): Boolean =
      self == other

    def hash(self: State): Int = self.hashCode

    def transition(state: State, cmd: Command): State = (state, cmd) match {
      case ((x, y, r), Left)         => (x - 1, y, r)
      case ((x, y, r), Right)        => (x + 1, y, r)
      case ((x, y, r), Up)           => (x, y - 1, r)
      case ((x, y, r), Down)         => (x, y + 1, r)
      case ((x, y, r), RotateRight)  => (x, y, (r + 1) % 4)
      case ((x, y, r), RotateLeft)   => (x, y, (r - 1) % 4)
    }
    def commands: List[Command] = List(Left, Right, Up, Down, RotateLeft, RotateRight)
    def distance(fst: State, other: State): Double = heuristic(fst, other)
    def distanceFromStart(cmds: List[Command]) = cmds.size
  }

  def heuristic(from: State, to: State): Double = {
    val h_diagonal = Math.min(Math.abs(from._1 - to._1), Math.abs(from._2 - to._2))
    val h_straight = (Math.abs(from._1 - to._1) + Math.abs(from._2 - to._2))

    // take rotation into account:
    val h_result   =
      (Math.sqrt(2) * h_diagonal + (h_straight - 2*h_diagonal)) + Math.abs(from._3 - to._3)

    val p = (1/10000)
    h_result * (1.0 + p)
  }

  val astar = AStarPrime[State, Command](
    (0, 0, 0),
    (1, 3, 2),
    engine
  )

  astar.computePath shouldBe
    Some(List(RotateRight, RotateRight, Right, Right, Right, Right, Down, Down, Left, Left, Down, Left))

}

package icfp2015

import scala.collection.JavaConversions._

import astarprime._

case class PathFinderPrime(game: GameState) {

  import game._

  val engine = new astarprime.Engine[GameUnit, Command] {
    def commands: List[Command] = List(
      Move(W), Move(E), Move(SW), Move(SE), Turn(true), Turn(false))

    def valid(self: GameUnit): Boolean = self.valid

    def bisimilar(self: GameUnit, other: GameUnit) =
      self.canonicalized == other.canonicalized

    def hash(self: GameUnit) = self.canonicalized.hashCode
    def transition(state: GameUnit, cmd: Command) = state exec cmd
    def distance(fst: GameUnit, other: GameUnit): Double = heuristic(fst, other)
  }

  def heuristic(from: GameUnit, to: GameUnit): Double = {

    val h_diagonal = Math.min(
      Math.abs(from.pivot.x - to.pivot.x),
      Math.abs(from.pivot.y - to.pivot.y))

    val h_straight = (Math.abs(from.pivot.x - to.pivot.x) + Math.abs(from.pivot.y - to.pivot.y))

    // take rotation into account:
    val h_result   =
      (Math.sqrt(2) * h_diagonal + (h_straight - 2*h_diagonal)) + (0.1 * Math.abs(from.orientation - to.orientation))

    val p = (1/10000)
    h_result * (1.0 + p)
  }

  def pathTo(target: GameUnit): Option[List[Command]] =
    AStarPrime(currentUnit.get, target, engine).computePath
}

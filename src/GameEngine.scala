package icfp2015

trait Game {

  type Filled = Boolean
  type Board = Array[Array[Filled]]

  object Board {
    def empty(width: Int, height: Int) = Array.fill[Filled](width, height)(false)
  }

  case class Score(score: Int, lastLines: Int) {
    // Compute the score for the given game unit and the
    // number of cleared lines
    def apply(unit: GameUnit, lines: Int): Score = {
      val points = unit.size + 100 * (1 + lines) * lines / 2
      val lineBonus: Int = if (lastLines > 1) {
        (lastLines - 1) * points / 10
      } else 0
      Score(points + lineBonus, lines)
    }
  }

  // Represents a single game
  case class GameState(
    board: Board,
    source: List[GameUnit],
    width: Int,
    height: Int,
    score: Score = Score(0, 0)
  ) {
    //val currentUnit: GameUnit

    def filled(c: Cell): Boolean = board(c.x)(c.y)

    implicit class CellOps(self: Cell) {

      def valid: Boolean =
        self.inside(Cell(0, 0), Cell(width - 1, height - 1)) && !filled(self)

      def apply(d: Direction): Option[Cell] = {
        val c = self.move(d)
        if (c.valid) Some(c) else None
      }
    }

    implicit class UnitOps(self: GameUnit) {
      def valid: Boolean =
        self.members.forall(_.valid)

      def apply(d: Direction): Option[GameUnit] = {
        val u = self.move(d)
        if (u.valid) Some(u) else None
      }

      def spawn: Option[GameUnit] = {
        import self._
        val minY = members.map(_.y).min //Seems to always be 0.
        assert(minY == 0) //Supporting other cases is not so easy.
        val minX = members.map(_.x).min //Not always 0.
        val maxX = members.map(_.x).max
        val unitWidth = maxX - minX + 1
        val expectedMinX: Int = (width - unitWidth) / 2
        val xDelta: Int = expectedMinX - minX
        val dir = if (xDelta < 0) W else E

        val newUnit = self.move(dir, xDelta.abs)
        if (newUnit.valid)
          Some(newUnit)
        else
          None
      }
    }
  }
  object GameState {

    def allGames(p: Problem): List[GameState] =
      p.sourceSeeds.map(GameState(p))

    def apply(p: Problem)(seed: Int): GameState = {
      import p._

      val board = Board.empty(width, height)

      val source: List[GameUnit] =
        List.iterate(RandomSource(seed).next, sourceLength)(_._2.next)
          .map (s => units(s._1 % units.size))

      filled.foreach { case Cell(x, y) => board(x)(y) = true }

      new GameState(board, source, width, height)
    }
  }
}
object Game extends Game

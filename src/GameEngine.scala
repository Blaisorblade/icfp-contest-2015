package icfp2015

trait Game {

  type Filled = Boolean
  type Board = Array[Array[Filled]]

  // Represents a single game
  class GameState(
    board: Board,
    source: List[GameUnit],
    width: Int,
    height: Int
  ) {
    //val currentUnit: GameUnit

    implicit class CellOps(self: Cell) {

      def valid: Boolean =
        self.inside(Cell(0, 0), Cell(width - 1, height - 1))

      def apply(d: Direction): Option[Cell] = {
        val c = self.move(d)
        if (c.valid) Some(c) else None
      }
    }
  }
  object GameState {

    def allGames(p: Problem): List[GameState] =
      p.sourceSeeds.map(GameState(p))

    def apply(p: Problem)(seed: Int): GameState = {
      import p._

      val board = Array.fill[Filled](width, height)(false)

      val source: List[GameUnit] =
        List.iterate(RandomSource(seed).next, sourceLength)(_._2.next)
          .map (s => units(s._1 % units.size))

      filled.foreach { case Cell(x, y) => board(x)(y) = true }

      new GameState(board, source, width, height)
    }
  }



}
object Game extends Game

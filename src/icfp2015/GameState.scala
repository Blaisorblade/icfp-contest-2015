package icfp2015

import GameState._

/**
 * Represents a single game.
 *
 * currentUnit is None only when the game has ended.
 */
case class GameState(
  board: Board,
  sourceIdxs: List[Int],
  units: List[GameUnit],
  seed: Int,
  width: Int,
  height: Int,
  currentUnit: Option[GameUnit],
  score: Score = Score(0, 0)
) {
  def source: List[GameUnit] = sourceIdxs map units

  def hasEnded: Boolean = currentUnit.isEmpty

  def filled(c: Cell): Boolean = board(c.x)(c.y)

  implicit class CellOps(self: Cell) {
    def inBoard =
      self.inside(Cell(0, 0), Cell(width - 1, height - 1))

    def valid: Boolean =
      inBoard && !filled(self)

    def apply(d: Direction): Option[Cell] = {
      val c = self.move(d)
      if (c.valid) Some(c) else None
    }
  }

  implicit class UnitOps(self: GameUnit) {
    def inBoard: Boolean =
      self.members.forall(_.inBoard)

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

  implicit class CommandOps(self: Command) {
    def producedUnit: GameUnit =
      currentUnit match {
        case None =>
          throw new IllegalArgumentException
        case Some(unit) =>
          (unit exec self)
      }

    def valid: Boolean =
      producedUnit.valid

    def inBoard: Boolean =
      producedUnit.inBoard
  }

  def updateCurrentUnit(newGameUnit: Option[GameUnit]) = copy(currentUnit = newGameUnit)
  def move(cmd: Command, expectedValid: Boolean): GameState = {
    assert(cmd.valid == expectedValid)
    if (cmd.valid)
      updateCurrentUnit(Some(currentUnit.get.exec(cmd)))
    else
      lockUnit()
  }

  def lockUnit(): GameState = {
    assert(currentUnit.isDefined)
    val gameUnitToLock = currentUnit.get
    val newBoard = board.clone()
    for (cell <- gameUnitToLock.members) {
      newBoard(cell.x)(cell.y) = true
    }
    var clearedRows = 0
    val rowsToCheck = gameUnitToLock.members.map(_.y).toSet.toList
    for {
      y <- 0 to height - 1//rowsToCheck.sorted
    } {
      val xs = 0 to width - 1
      val isRowFull = xs forall (x => newBoard(x)(y))
      if (isRowFull) {
        clearedRows += 1
        //Copy downward all rows above the current one
        for {
          newY <- y - 1 to (0, -1)
          newX <- 0 to width - 1
        } {
          //Copy the array pointer? No, we store the array in the wrong order for that.
          newBoard(newX)(newY + 1) = newBoard(newX)(newY)
        }
        //Clear first row
        for (newX <- 0 to width - 1)
          newBoard(newX)(0) = false
      }
    }
    val newSourceIdxs =
      if (sourceIdxs.nonEmpty)
        sourceIdxs.tail
      else
        Nil
    val newUnit = source.headOption.flatMap(_.spawn)

    GameState(newBoard, newSourceIdxs, units, seed, width, height, newUnit, score(gameUnitToLock, clearedRows))
  }
}

object GameState {
  type Filled = Boolean
  type Board = Array[Array[Filled]]

  def allGames(p: Problem): List[GameState] =
    p.sourceSeeds.map(GameState(p))

  def apply(p: Problem)(seed: Int): GameState = {
    import p._

    val board = Board.empty(width, height)

    val sourceIdxs: List[Int] =
      List.iterate(RandomSource(seed).next, sourceLength)(_._2.next)
        .map (s => s._1 % units.size)

    filled.foreach { case Cell(x, y) => board(x)(y) = true }

    //Preload the first GameUnit
    GameState(board, sourceIdxs.tail, units, seed, width, height, Some(units(sourceIdxs.head)))
  }
}

object Board {
  def empty(width: Int, height: Int) = Array.fill[Filled](width, height)(false)
}

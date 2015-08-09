package icfp2015

// Unit -> GameUnit
trait Direction
case object NW extends Direction
case object NE extends Direction
case object W  extends Direction
case object E  extends Direction
case object SW extends Direction
case object SE extends Direction

object Direction {
  //Angle w.r.t West (Clockwise) = 60 deg. * id
  def toId(d: Direction) =
    d match {
      case W  => 0
      case NW => 1
      case NE => 2
      case E  => 3
      case SE => 4
      case SW => 5
    }

  val dirs = List(W, NW, NE, E, SE, SW)

  def toDir(id: Int): Direction = dirs(id)

  def dir(clockwise: Boolean) = if (clockwise) 1 else - 1
  def rotate(d: Direction, clockwise: Boolean) = toDir((toId(d) + dir(clockwise)) % 6)

  def rotateClockwise(d: Direction) = rotate(d, true)
  def rotateCounterClockwise(d: Direction) = rotate(d, false)
}

// Input Data
case class Problem(
  id: Int,
  units: List[GameUnit],
  width: Int,
  height: Int,
  filled: List[Cell],
  sourceLength: Int,
  sourceSeeds: List[Int])

//Internal coordinate system. Also vectors.
case class QCell(x: Int, y: Int) {
  def +(other: QCell) = QCell(x + other.x, y + other.y)
  def -(other: QCell) = QCell(x - other.x, y - other.y)
  //Rotate clockwise around the origin.
  def rotate60CW = QCell(x - y, x)
  def rotate60CCW = QCell(y, y - x)
  def rotate(clockwise: Boolean): QCell =
    if (clockwise) rotate60CW else rotate60CCW

  def toCell: Cell = Cell.tupled(toHex(x, y))
}
object QCellUtil {
  def fromCell(c: Cell): QCell = QCell.tupled(fromHex(c.x, c.y))
  val dirs =
    List((-1, 0), (-1, -1), (0, -1), (1, 0), (1, 1), (0, 1)) map (QCell.tupled)
  def fromDir(d: Direction) = dirs(Direction.toId(d))
}

//Official coordinate system.
case class Cell(x: Int, y: Int) {
  def move2(d: Direction): Cell =
    (QCellUtil.fromCell(this) + QCellUtil.fromDir(d)).toCell

  def move(d: Direction): Cell = Cell.tupled(d match {
    case W  => (x - 1, y)
    case E  => (x + 1, y)
    case _ =>
      val corr = y % 2
      d match {
        case NW => (x - 1 + corr, y - 1)
        case NE => (x + corr, y - 1)
        case SW => (x - 1 + corr, y + 1)
        case SE => (x + corr, y + 1)
      }
  })

  def directionTo(other: Cell): Direction = other match {
    case Cell(ox, oy) if oy == y     && ox == (x + 1)           => E
    case Cell(ox, oy) if oy == y     && ox == (x - 1)           => W
    case Cell(ox, oy) if oy == y + 1 && ox == (x + (y % 2) - 1) => SW
    case Cell(ox, oy) if oy == y + 1 && ox == (x + (y % 2))     => SE
    case other@Cell(ox, oy) =>
      sys error s"""cannot move from $this to $other:
        $oy ${y + 1}
        $ox ${x - (y % 2) + 1}"""
  }

  // Checks whether the cell is inside of a given rectangle
  def inside(upperLeft: Cell, lowerRight: Cell): Boolean =
    x >= upperLeft.x && y >= upperLeft.y &&
    x <= lowerRight.x && y <= lowerRight.y
}

// orientation = (0 - 5)
case class GameUnit(members: List[Cell], pivot: Cell, orientation: Int = 0) {

  def move(d: Direction): GameUnit = GameUnit(
    members map (_ move d),
    pivot move d)

  def move(d: Direction, n: Int): GameUnit =
    if (n == 0)
      this
    else
      move(d).move(d, n - 1)

  def move(to: Cell): GameUnit = {
    val qPivot = QCellUtil.fromCell(pivot)
    val qTo = QCellUtil.fromCell(to)

    val deltaQ = qTo - qPivot

    GameUnit(members map (c => (QCellUtil.fromCell(c) + deltaQ).toCell), to)
  }
  def rotate(clockwise: Boolean): GameUnit = {
    val qPivot = QCellUtil.fromCell(pivot)
    val newMembers = members.map(member => (qPivot + (QCellUtil.fromCell(member) - qPivot).rotate(clockwise)).toCell)
    val neworientation = if (clockwise) (orientation + 1) % 6 else (orientation - 1) % 6
    GameUnit(newMembers, pivot, neworientation)
  }

  def exec(c: Command): GameUnit = c match {
    case Turn(clockwise) => rotate(clockwise)
    case Move(dir) => move(dir)
  }

  def size: Int = members.size

  def canonicalized = CanonicalGameUnit(members.toSet, pivot)
}

case class CanonicalGameUnit(members: Set[Cell], pivot: Cell)

sealed trait Command

case class Move(dir: Direction) extends Command {
  assert (dir != NW && dir != NE)
}
case class Turn(clockwise: Boolean) extends Command

object Command {
  /**
   * Return possible encodings (packed as a string).
   */
  val toCharsMap = Map[Command, String](
      Move(W)     -> "p'!.03",
      Move(E)     -> "bcefy2",
      Move(SW)    -> "aghij4",
      Move(SE)    -> "lmno 5",
      Turn(true)  -> "dqrvz1",
      Turn(false) -> "kstuwx"
    )

  def toChars(c: Command): String =
    toCharsMap.getOrElse(c, throw new IllegalArgumentException)

  val fromChar =
    (for {
      (cmd, chars) <- toCharsMap
      c <- chars
    } yield (c, cmd)).toMap

  /**
   * Return
   */
  def toChar(c: Command) =
    toChars(c).charAt(0)
  def toSolution(commands: Seq[Command]): String = new String(commands.map(toChar).toArray)
  def fromSolution(cmdString: String): Seq[Command] = cmdString.map(fromChar)
}

// Output Data
// for now: Command = Char
case class Output(
  problemId: Int,
  seed: Int,
  tag: String,
  solution: String)


case class Score(score: Int, lastLines: Int) {
  // Compute the score for the given game unit and the
  // number of cleared lines
  def apply(unit: GameUnit, lines: Int): Score = {
    val points = unit.size + 100 * (1 + lines) * lines / 2
    val lineBonus: Int = if (lastLines > 1) {
      (lastLines - 1) * points / 10
    } else 0
    Score(score + points + lineBonus, lines)
  }
}

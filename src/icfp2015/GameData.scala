package icfp2015

import spray.json._

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

case class Cell(x: Int, y: Int) {
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

case class GameUnit(members: List[Cell], pivot: Cell) {
  def move(d: Direction): GameUnit = GameUnit(
    members map (_ move d),
    pivot move d)

  def move(d: Direction, n: Int): GameUnit =
    if (n == 0)
      this
    else
      move(d).move(d, n - 1)

  def move(to: Cell): GameUnit = {
    val (qxp, qyp) = fromHex(pivot.x, pivot.y)
    val (qxt, qyt) = fromHex(to.x, to.y)

    val dqx = qxt - qxp
    val dqy = qyt - qyp

    GameUnit(members map { case Cell(x, y) =>
      val (qx, qy) = fromHex(x, y)
      Cell.tupled(toHex(qx + dqx, qy + dqy))
    }, to)
  }

  def exec(c: Command): GameUnit = c match {
    case Turn(_) => ???
    case Move(dir) => move(dir)
  }

  def size: Int = members.size
}


sealed trait Command

case class Move(dir: Direction) extends Command {
  assert (dir != NW && dir != NE)
}
case class Turn(clockwise: Boolean) extends Command

object Command {
  /**
   * Return possible encodings (packed as a string).
   */
  def toChars(c: Command): String =
    c match {
      case Move(W)     =>
        "p'!.03"
      case Move(E)     =>
        "bcefy2"
      case Move(SW)    =>
        "aghij4"
      case Move(SE)    =>
        "lmno 5"
      case Move(_)     =>
        throw new IllegalArgumentException
      case Turn(true)  =>
        "dqrvz1"
      case Turn(false) =>
        "kstuwx"
    }
  /**
   * Return
   */
  def toChar(c: Command) =
    toChars(c).charAt(0)
  def toSolution(commands: Seq[Command]): String = new String(commands.map(toChar).toArray)
}

// Output Data
// for now: Command = Char
case class Output(
  problemId: Int,
  seed: Int,
  tag: String,
  solution: String)


object HoneycombProtocol extends DefaultJsonProtocol {
  implicit val cellFormat    = jsonFormat2(Cell)
  implicit val unitFormat    = jsonFormat2(GameUnit)
  implicit val problemFormat = jsonFormat7(Problem)

  implicit val outputFormat  = jsonFormat4(Output)
}


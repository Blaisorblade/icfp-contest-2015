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
  // Checks whether the cell is inside of a given rectangle
  def inside(upperLeft: Cell, lowerRight: Cell): Boolean =
    x >= upperLeft.x && y >= upperLeft.y &&
    x <= lowerRight.x && y <= lowerRight.y
}

case class GameUnit(
  members: List[Cell],
  pivot: Cell)


// Output Data
// for now: Command = Char
case class Output(
  problemId: Int,
  seed: Int,
  tag: String,
  solution: List[Char])


object HoneycombProtocol extends DefaultJsonProtocol {
  implicit val cellFormat    = jsonFormat2(Cell)
  implicit val unitFormat    = jsonFormat2(GameUnit)
  implicit val problemFormat = jsonFormat7(Problem)

  implicit val outputFormat  = jsonFormat4(Output)
}


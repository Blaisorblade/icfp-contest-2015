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


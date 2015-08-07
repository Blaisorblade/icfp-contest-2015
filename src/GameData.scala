package icfp2015

import spray.json._

// Unit -> GameUnit

// Input Data
case class Problem(
  id: Int,
  units: List[GameUnit],
  width: Int,
  height: Int,
  filled: List[Cell],
  sourceLength: Int,
  sourceSeeds: List[Int])

case class Cell(x: Int, y: Int)

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
  implicit val problemFormat = jsonFormat7(Problem.apply)

  implicit val outputFormat  = jsonFormat4(Output)
}


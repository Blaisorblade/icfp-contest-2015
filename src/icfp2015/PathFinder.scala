package icfp2015

import scala.collection.JavaConversions._

import astar._
import astar.heuristics._

import Game._
case class PathFinder(game: GameState) {

  import game._

  val start = currentUnit.get
  val (qstartx, qstarty) = fromHex(start.pivot.x, start.pivot.y)

  def fromHex(x: Int, y: Int): (Int, Int) =
    (x + (y + 1) / 2, y)

  def toHex(x: Int, y: Int): (Int, Int) =
    (x - (y + 1) / 2, y)

  val (qwidth, qheight) = fromHex(game.width, game.height)

  def pathTo(target: Cell): Option[List[Command]] = {

    val (qtargetx, qtargety) = fromHex(target.x, target.y)

    val g = new GameInterface[GameUnit] {
      def width = qwidth
      def height = qheight
      def canPlaceAt(el: GameUnit, qx: Int, qy: Int) = {
        val (x, y) = toHex(qx, qy)
        el.move(Cell(x, y)).valid
      }
      def startLocationX = qstartx
      def startLocationY = qstarty
      def targetLocationX = qtargetx
      def targetLocationY = qtargety
    }

    val area = new AreaMap[GameUnit](g)
    val astar = new AStar(area, start, new DiagonalHeuristic)

    val res = astar.calcShortestPath

    if (res == null) return None

    // We are there...
    if (start.pivot == target) return Some(Nil)

    val cellPath: List[Cell] = res.toList map {
      p => Cell.tupled(toHex(p.x, p.y))
    }

    // only works if start != target
    val directions = cellPath
      .foldLeft[(Cell, List[Direction])]((start.pivot, Nil)) {
        case ((curr, dirs), nxt) => (nxt, dirs :+ (curr directionTo nxt))
      }

    Some(directions._2 map Move)
  }
}

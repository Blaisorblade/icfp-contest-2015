package icfp2015

import scala.collection.JavaConversions._

import CDIO.pathFinder._
import CDIO.pathFinder.heuristics._

trait PathFinder { self: Game =>

  class PathFinderEngine(game: GameState) {

    import game._

    val start = currentUnit.get

    def fromHex(x: Int, y: Int): (Int, Int) =
      (x + (y + 1) / 2, y)

    def toHex(x: Int, y: Int): (Int, Int) =
      (x - (y + 1) / 2, y)

    val (qwidth, qheight) = fromHex(game.width, game.height)

    def pathTo(target: Cell): List[Command] = {

      val (qtargetx, qtargety) = fromHex(target.x, target.y)

      val g = new GameInterface[GameUnit] {
        def width = qwidth
        def height = qheight
        def canPlaceAt(el: GameUnit, qx: Int, qy: Int) = {
          val (x, y) = toHex(qx, qy)
          el.move(Cell(x, y)).valid
        }
        def startLocationX = 0
        def startLocationY = 0
        def targetLocationX = qtargetx
        def targetLocationY = qtargety
      }

      val area = new AreaMap[GameUnit](g)
      val astar = new AStar(area, start, new DiagonalHeuristic)

      val cellPath: List[Cell] = astar.calcShortestPath.toList map {
        p => Cell.tupled(toHex(p.x, p.y))
      }

      // only works if start != target
      val directions = (cellPath :+ target).foldLeft[(Cell, List[Direction])]((start.pivot, Nil)) {
        case ((curr, dirs), nxt) => (nxt, dirs :+ (curr directionTo nxt))
      }

      directions._2 map Move
    }
  }

}

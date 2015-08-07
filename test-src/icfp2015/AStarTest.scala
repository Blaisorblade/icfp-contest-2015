package icfp2015
package test

import org.scalatest._
import org.scalatest.matchers._

import scala.collection.JavaConversions._

import CDIO.pathFinder._
import CDIO.pathFinder.heuristics._

class AStarTest extends FunSpec with Matchers {

  val dummyUnit = GameUnit(Nil, Cell(0, 0))

  def fromHex(x: Int, y: Int): (Int, Int) =
    (x + (y + 1) / 2, y)

  def toHex(x: Int, y: Int): (Int, Int) =
    (x - (y + 1) / 2, y)

  val blocked = List(
    (2, 2), (3, 2), (4, 2), (0, 4), (1, 4), (2, 4)
  )

  val w = 5
  val h = 10

  val game = new GameInterface[GameUnit] {
    def width = fromHex(w, h)._1
    def height = fromHex(w, h)._2
    def canPlaceAt(el: GameUnit, x: Int, y: Int) =
      !(blocked contains toHex(x, y)) &&
        (toHex(x, y) match {
          case (x2, y2) => x2 >= 0 && y2 >= 0 && x2 < w && y2 < h
        })
    def startLocationX = 0
    def startLocationY = 0
    def targetLocationX = fromHex(2, 6)._1
    def targetLocationY = fromHex(2, 6)._2
  }

  val area = new AreaMap[GameUnit](game)
  val astar = new AStar(area, dummyUnit, new DiagonalHeuristic)

  val res = astar.calcShortestPath

  if (res == null)
    println("Sorry, could'nt find a path")

  else
    println(res.toList map {
      p => toHex(p.x, p.y)
    })
}

package icfp2015
package test

import org.scalatest._
import org.scalatest.matchers._

class GameVisualizationTest extends FunSpec with Matchers {

  object Game extends Game with GameVisualization

  import Game._

  val board = Board.empty(5, 15)
  board(0)(14) = true
  board(1)(14) = true
  board(4)(14) = true

  val state = GameState(board, Nil, 5, 15)

  renderToFile(state, "test.html")

}

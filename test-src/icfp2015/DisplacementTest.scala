package icfp2015
package test

import org.scalatest._
import org.scalatest.matchers._


class DisplacementTest extends FunSpec with Matchers {
  describe("Rotation") {
    List.iterate[Direction](W, 6)(Direction.rotateClockwise) shouldBe
      Direction.dirs
    List.iterate[Direction](SW, 6)(Direction.rotateCounterClockwise).reverse shouldBe
      Direction.dirs
  }

  describe("Move Cell(2, 4)") {
    val c = Cell(2, 4)
    c.move(NW) shouldBe Cell(1, 3)
    c.move(NE) shouldBe Cell(2, 3)
    c.move(SW) shouldBe Cell(1, 5)
    c.move(SE) shouldBe Cell(2, 5)
    c.move(W)  shouldBe Cell(1, 4)
    c.move(E)  shouldBe Cell(3, 4)
  }

  describe("Move Cell(1, 1)") {
    val c = Cell(1, 1)
    c.move(NW) shouldBe Cell(1, 0)
    c.move(NE) shouldBe Cell(2, 0)
    c.move(SW) shouldBe Cell(1, 2)
    c.move(SE) shouldBe Cell(2, 2)
    c.move(W)  shouldBe Cell(0, 1)
    c.move(E)  shouldBe Cell(2, 1)
  }

  import Game._

  val dummyGameUnit = GameUnit(Nil, Cell(0, 0))

  val aGameState = GameState(Board.empty(10, 10), Nil, -1, 10, 10, Some(dummyGameUnit))

  describe("Move/check Cell(1, 1)") {
    import aGameState._

    val c = Cell(0, 0)
    c(NW) shouldBe None
    c(W) shouldBe None
    c(NE) shouldBe None
    c(SW) shouldBe None
    c(E) shouldBe Some(Cell(1, 0))
  }

  val aGameUnit1 = GameUnit(List(Cell(0, 0), Cell(1, 0), Cell(1, 1)), Cell(0, 0))
  val aGameUnit1b = GameUnit(List(Cell(0, 1), Cell(1, 1), Cell(1, 2)), Cell(0, 0))
  val aGameUnit2 = GameUnit(List(Cell(0, 0), Cell(2, 0)), Cell(1, 0))
  val aGameUnit2b = GameUnit(List(Cell(0, 1), Cell(2, 1)), Cell(1, 1))

  describe("Spawn game units") {
    import aGameState._
    it("should work") {
      aGameUnit1.spawn shouldBe Some(GameUnit(List(Cell(4, 0), Cell(5, 0), Cell(5, 1)), Cell(4, 0)))
      aGameUnit2.spawn shouldBe Some(GameUnit(List(Cell(3, 0), Cell(5, 0)), Cell(4, 0)))
    }

    ignore("should work even for units not starting on the top row"){
      aGameUnit1b.spawn shouldBe Some(GameUnit(List(Cell(4, 0), Cell(5, 0), Cell(5, 1)), Cell(4, 0)))
      aGameUnit2b.spawn shouldBe Some(GameUnit(List(Cell(3, 0), Cell(5, 0)), Cell(4, 0)))
    }
  }
}
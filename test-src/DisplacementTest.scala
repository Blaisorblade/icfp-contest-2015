package icfp2015
package test

import org.scalatest._
import org.scalatest.matchers._

class DisplacementTest extends FunSpec with Matchers {

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

}

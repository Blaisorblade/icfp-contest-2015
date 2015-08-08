package icfp2015

import org.scalatest._

class SimplePlayerTest extends FunSpec with Matchers {
  describe("Simple") {
    it("should play more GameUnits") {
      println(SimplePlayer.solve(ProblemLoader.problems(0)))
    }
  }
}

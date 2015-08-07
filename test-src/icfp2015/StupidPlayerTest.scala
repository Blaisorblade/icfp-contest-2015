package icfp2015

import org.scalatest._

class StupidPlayerTest extends FunSpec with Matchers {
  describe("StupidPlayer") {
    it("should play more GameUnits") {
      StupidPlayer.solve(ProblemLoader.problems(0))
    }
  }
}

package icfp2015

import org.scalatest._

/**
 * @author pgiarrusso
 */
class RandomSourceTest extends FunSpec with Matchers {
  val testSeed = 17
  val sample = RandomSource(testSeed)
  def sampleN(r: RandomSource, n: Int) = List.iterate(r.next, n)(_._2.next) map (_._1)
  val expected = List(0,24107,16552,12125,9427,13152,21440,3383,6873,16117)
  val sample2 = new RandomSourceState(testSeed)

  describe("sampleN") {
    it("should work as exemplified") {
      sampleN(sample, expected.length) shouldBe expected
    }
  }
}

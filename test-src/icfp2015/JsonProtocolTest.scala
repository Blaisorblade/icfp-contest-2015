package icfp2015
package test

import org.scalatest._
import org.scalatest.matchers._

class JsonProtocolTest extends FunSpec with Matchers {
  val problem1 = """{"height":10,"width":10,"sourceSeeds":[0],"units":[{"members":[{"x":0,"y":0}],"pivot":{"x":0,"y":0}},{"members":[{"x":0,"y":0},{"x":2,"y":0}],"pivot":{"x":1,"y":0}},{"members":[{"x":0,"y":0},{"x":0,"y":2}],"pivot":{"x":0,"y":1}},{"members":[{"x":2,"y":0},{"x":0,"y":1},{"x":2,"y":2}],"pivot":{"x":1,"y":1}},{"members":[{"x":0,"y":0},{"x":1,"y":1},{"x":0,"y":2}],"pivot":{"x":0,"y":1}},{"members":[{"x":0,"y":0},{"x":1,"y":0}],"pivot":{"x":0,"y":0}},{"members":[{"x":0,"y":0},{"x":1,"y":0}],"pivot":{"x":1,"y":0}},{"members":[{"x":0,"y":0},{"x":0,"y":1}],"pivot":{"x":0,"y":0}},{"members":[{"x":0,"y":0},{"x":0,"y":1}],"pivot":{"x":0,"y":1}},{"members":[{"x":0,"y":0},{"x":1,"y":0},{"x":2,"y":0}],"pivot":{"x":0,"y":0}},{"members":[{"x":0,"y":0},{"x":1,"y":0},{"x":2,"y":0}],"pivot":{"x":1,"y":0}},{"members":[{"x":0,"y":0},{"x":1,"y":0},{"x":2,"y":0}],"pivot":{"x":2,"y":0}},{"members":[{"x":0,"y":0},{"x":0,"y":1},{"x":0,"y":2}],"pivot":{"x":0,"y":0}},{"members":[{"x":0,"y":0},{"x":0,"y":1},{"x":0,"y":2}],"pivot":{"x":0,"y":1}},{"members":[{"x":0,"y":0},{"x":0,"y":1},{"x":0,"y":2}],"pivot":{"x":0,"y":2}},{"members":[{"x":1,"y":0},{"x":0,"y":1},{"x":1,"y":2}],"pivot":{"x":1,"y":0}},{"members":[{"x":1,"y":0},{"x":0,"y":1},{"x":1,"y":2}],"pivot":{"x":1,"y":1}},{"members":[{"x":1,"y":0},{"x":0,"y":1},{"x":1,"y":2}],"pivot":{"x":1,"y":2}}],"id":0,"filled":[],"sourceLength":100}"""

  import HoneycombProtocol._
  import spray.json._

  describe("Converting Json input") {
    val res = problem1.parseJson.convertTo[Problem]
    res shouldBe a [Problem]
    res.height shouldBe (10)
    res.sourceSeeds shouldBe (List(0))
  }

}

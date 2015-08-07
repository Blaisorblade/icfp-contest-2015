package icfp2015

import scala.io._
/**
 * @author pgiarrusso
 */

object ProblemLoader {
  def load(path: String) = new String(Source.fromFile(path).to[Array])

  import HoneycombProtocol._
  import spray.json._

  val problems = for {
    i <- 0 to 23
  } yield load(s"problems/problem_$i.json").parseJson.convertTo[Problem]

  //Now we can do stats. For instance:
  //ProblemLoader.problems.map(_.sourceLength).zipWithIndex.sortBy(_._1)
}
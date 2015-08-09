package icfp2015

import scala.io._
/**
 * @author pgiarrusso
 */

object ProblemLoader {
  def load(path: String) = new String(Source.fromFile(path).to[Array])

  import HoneycombProtocol._
  import spray.json._

  val inputFiles =
    if (Driver.globalConfig.fileNames.nonEmpty)
      Driver.globalConfig.fileNames
    else
      for {
        i <- 0 to 24
      } yield s"problems/problem_$i.json"
  val problems = inputFiles.map(load(_).parseJson.convertTo[Problem])

  //Now we can do stats. For instance:
  //ProblemLoader.problems.map(_.sourceLength).zipWithIndex.sortBy(_._1)

  val sol = "iiiiiiimimiiiiiimmimiiiimimimmimimimimmeemmimimiimmmmimmimiimimimmimmimeeemmmimimmimeeemiimiimimimiiiipimiimimmmmeemimeemimimimmmmemimmimmmiiimmmiiipiimiiippiimmmeemimiipimmimmipppimmimeemeemimiieemimmmm"
  val solProb = 6
  val solSeed = 0
  val loadSolution = Output(6, 0, "", sol)
  //TODO: visualize and compare that solution with the official video.

  def solve(player: Player) = {
    val (outputs, scores) = (for {
      prob <- problems.par
      sol = player.solve(prob)
      _ = println(s"Problem ${prob.id}: score ${sol._2}")
    } yield sol).unzip

    outputs.flatten
  }

  def outputJsonSolution =
    //NOTE: Use StupidPlayer for the other, more stupid but safer, heuristic.
    Console.err.println(ProblemLoader.solve(SimplePlayer).toList.toJson)
}

case class Config(fileNames: List[String] = Nil, timeLimit: Int = -1, memoryLimit: Int = -1, cores: Int = -1, phrases: List[String] = Nil)
object Driver {
  var globalConfig = Config()

  def main(args: Array[String]) {
    import scopt._
    val parser = new OptionParser[Unit]("scopt") {
      head("icfpc")
      opt[String]('f', "filename") foreach { x =>
        globalConfig = globalConfig.copy(fileNames = globalConfig.fileNames :+ x)
      } text ("File containing JSON encoded input (can be given multiple times)")
      opt[Int]('t', "timeLimit") foreach { x =>
        globalConfig = globalConfig.copy(timeLimit = x)
      } text ("Time limit, in seconds, to produce output")
      opt[Int]('m', "memoryLimit") foreach { x =>
        globalConfig = globalConfig.copy(memoryLimit = x)
      } text ("Memory limit, in megabytes, to produce output")
      opt[Int]('c', "cores") foreach { x =>
        globalConfig = globalConfig.copy(cores = x)
      } text ("Number of processor cores available")
      opt[String]('p', "phraseOfPower") foreach { x =>
        globalConfig = globalConfig.copy(phrases = globalConfig.phrases :+ x)
      } text ("Phrase of power (can be given multiple times)")
      help("help") text ("prints this usage text")
    }
    if (parser.parse(args)) {
      ProblemLoader.outputJsonSolution
    } else {
      // arguments are bad, usage message will have been displayed
    }
  }
}

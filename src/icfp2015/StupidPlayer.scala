package icfp2015

/**
 * @author pgiarrusso
 */

trait Player {
  def solve(p: Problem): (List[Output], Int)
}

import GameVisualization._

object StupidPlayer extends Player {
  val time = new java.util.Date

  val viz = new Visualizer

  def solve(p: Problem): (List[Output], Int) = viz.problem(p) {
    val (outputs, scores) = (GameState.allGames(p).map { game =>
      viz.game(game) { solveState(p.id, s"Stupid ($time)")(game) }
    }).unzip

    val problemScore = scores.map(_.score).sum / scores.length
    (outputs, problemScore)
  }

  def solveState(id: Int, tag: String)(g: GameState): (Output, Score) = {
    //Generate possible next moves
    //val possibleMoves: List[Direction]
    //Evaluate solutions
    //Choose the best.
    assert(!g.hasEnded)
    val (commands, score) = go(g, Nil, Stream.iterate(List(SW, SE) map Move)(identity))
    val solution = Command.toSolution(commands)
    (Output(id, g.seed, tag, solution), score)
  }

  def go(gameState: GameState, commandsAcc: List[Command], commandsProposals: Seq[Seq[Command]]): (List[Command], Score) = {
    import gameState._
    assert(!hasEnded)
    assert(currentUnit.isDefined)
    //Pick command
    val commandOptions = commandsProposals.head

    //Check if legal.
    //If all are illegal, we pick an arbitrary one (the first) to lock the piece
    val (next, valid) = commandOptions.filter(_.valid).headOption match {
      case None =>
        ((commandOptions :+ Move(SW)).head, false)
      case Some(next) =>
        (next, true)
    }
    viz.step(gameState, next, lock = !valid)
    val newGameState = gameState.move(next, valid)
    val newCommands = commandsAcc :+ next

    if (newGameState.hasEnded) {
      (newCommands, newGameState.score)
    } else {
      go(newGameState, newCommands, commandsProposals.tail)
    }
  }
}

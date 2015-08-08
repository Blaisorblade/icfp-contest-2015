package icfp2015

/**
 * @author pgiarrusso
 */

trait Player {
  def solve(p: Problem): (List[Output], Int)
}

object StupidPlayer extends Player {
  val time = new java.util.Date

  def solve(p: Problem): (List[Output], Int) = {
    val (outputs, scores) = (GameState.allGames(p) map solveState(p.id, s"Stupid ($time)")).unzip
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
    val newGameState = gameState.move(next, valid)
    val newCommands = commandsAcc :+ next

    if (newGameState.hasEnded) {
      println(newGameState.score)
      (newCommands, newGameState.score)
    } else {
      go(newGameState, newCommands, commandsProposals.tail)
    }
  }
}

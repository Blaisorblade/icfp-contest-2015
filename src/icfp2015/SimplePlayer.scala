package icfp2015

import Game._

object SimplePlayer extends Player {

  val time = new java.util.Date

  def solve(p: Problem): (List[Output], Int) = {
    val (outputs, scores) = (GameState.allGames(p) map solveState(p.id, s"Simple ($time)")).unzip
    val problemScore = scores.map(_.score).sum / scores.length
    (outputs, problemScore)
  }

  def solveState(id: Int, tag: String)(g: GameState): (Output, Score) = {
    //Generate possible next moves
    //val possibleMoves: List[Direction]
    //Evaluate solutions
    //Choose the best.
    assert(!g.hasEnded)
    val (commands, score) = go(g, Nil)
    val solution = Command.toSolution(commands)
    (Output(id, g.seed, tag, solution), score)
  }

  def go(gameState: GameState, commandsAcc: List[Command]): (List[Command], Score) = {
    import gameState._
    assert(!hasEnded)
    assert(currentUnit.isDefined)

    //Pick command
    val pf = PathFinder(gameState)

    // order possible pivot-slots by priority
    val solutions = for {
      row <- ((height - 1) to 0 by -1).view
      col <- 0 until width
      c = Cell(col, row)
      p <- pf pathTo c
    } yield (c, p)

    val commandsProposals = (solutions.headOption match {
      case Some((_, cmds)) => (cmds.toStream map (x => List(x))) ++ Stream.iterate(Nil: List[Command])(identity)
      case None => Stream.iterate(List(SW, SE) map Move)(identity)
    })

    val (newGameState, newCommands) = nestedGo(gameState, commandsAcc, commandsProposals)
    if (newGameState.hasEnded) {
      println(newGameState.score)
      (newCommands, gameState.score)
    } else {
      go(newGameState, newCommands)
    }
  }

  def nestedGo(gameState: GameState, commandsAcc: List[Command], commandsProposals: Seq[Seq[Command]]): (GameState, List[Command]) = {
    import gameState._
    val commandOptions = commandsProposals.head
    val (newGameState, next, doStop) = commandOptions.filter(_.valid).headOption match {
      case None =>
        (gameState.lockUnit(), (commandOptions :+ Move(SW)).head, true)
      case Some(next) =>
        (gameState.move(next), next, false)
    }
    val newCommands = commandsAcc :+ next
    if (newGameState.hasEnded || doStop) {
      (gameState, newCommands)
    } else
      nestedGo(newGameState, newCommands, commandsProposals.tail)
  }
}

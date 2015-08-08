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

  def go(gameState: GameState, commands: List[Command]): (List[Command], Score) = {
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

    //Move(SW) is a command that will fail, hence ensuring that the unit locks.
    val (newgame, cmds) = solutions.headOption match {
      case Some((pos, cmds)) =>
        (gameState.move(pos).lockUnit(), commands ++ cmds :+ Move(SW))

      case None => (gameState.lockUnit(), commands :+ Move(SW))
    }

    if (newgame.hasEnded) {
      println(newgame.score)
      (cmds, newgame.score)
    } else {
      go(newgame, cmds)
    }
  }
}

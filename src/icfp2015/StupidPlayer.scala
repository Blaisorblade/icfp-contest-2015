package icfp2015

import Game._

/**
 * @author pgiarrusso
 */

trait Player {
  def solve(p: Problem): List[Output]
}

object StupidPlayer extends Player {
  def solve(p: Problem): List[Output] = {
    GameState.allGames(p) map solveState(p.id, "")
  }

  def solveState(id: Int, tag: String)(g: GameState): Output = {
    //Generate possible next moves
    //val possibleMoves: List[Direction]
    //Evaluate solutions
    //Choose the best.
    assert(!g.hasEnded)
    val commands = go(g, Nil)
    val solution = Command.toSolution(commands)
    Output(id, g.seed, tag, solution)
  }

  def go(gameState: GameState, commands: List[Command]): List[Command] = {
    import gameState._
    assert(!hasEnded)
    assert(currentUnit.isDefined)
    //Pick command
    val commandOptions = List(SW, SE) map Move
    //Check if if legal.
    //If all are illegal, we pick an arbitrary one (the first) to lock the piece
    val (newGameState, next) = commandOptions.filter(_.valid).headOption match {
      case None =>
        (gameState.lockUnit(), commandOptions.head)
      case Some(next) =>
        (gameState.move(next), next)
    }
    val newCommands = commands :+ next
    if (newGameState.hasEnded)
      newCommands
    else
      go(newGameState, newCommands)
  }
}

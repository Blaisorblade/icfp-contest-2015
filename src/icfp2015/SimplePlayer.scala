package icfp2015

import GameVisualization._

object SimplePlayer extends Player {

  val time = new java.util.Date

  val viz = new DummyVisualizer

  def solve(p: Problem): (List[Output], Int) = viz.problem(p) {
    val (outputs, scores) = (GameState.allGames(p).map { game =>
      viz.game(game) { solveState(p.id, s"Simple ($time)")(game) }
    }).unzip

    val problemScore = scores.map(_.score).sum / scores.length
    (outputs, problemScore)
  }

  //For debug
  val focusedProbId = -1
  val dumpedFrames = 10

  def solveState(id: Int, tag: String)(g: GameState): (Output, Score) = {
    //Generate possible next moves
    //val possibleMoves: List[Direction]
    //Evaluate solutions
    //Choose the best.
    assert(!g.hasEnded)
    val verbose = id == focusedProbId
    if (verbose)
      renderToFile(g, "test.html")

    val (commands, score) = go(g, Nil, verbose, frame = 0)
    val solution = Command.toSolution(commands)
    (Output(id, g.seed, tag, solution), score)
  }

  var i = 0;
  def go(gameState: GameState, commandsAcc: List[Command], verbose: Boolean, frame: Int): (List[Command], Score) = {
    i += 1

    import gameState._
    assert(!hasEnded)
    assert(currentUnit.isDefined)

    //Pick command
    val pf = PathFinderPrime(gameState)

    val curr = gameState.currentUnit.get

    // order possible pivot-slots by priority
    val solutions = for {
      row <- (height to 0 by -1).view
      col <- 0 until (width + 4)
      rot <- 0 until curr.symmetry
      rotated = curr rotate rot
      newCurr = rotated move Cell(col, row)
      if newCurr.valid
      p <- pf pathTo newCurr
    } yield p

    val commandsProposals = (solutions.headOption match {
      case Some(cmds) => (cmds.toStream map (x => List(x))) ++ Stream.iterate(Nil: List[Command])(identity)
      case None => Stream.iterate(List(SW, SE) map Move)(identity)
    })

    val (newGameState, newCommands) = nestedGo(gameState, commandsAcc, commandsProposals)
    if (verbose && frame < dumpedFrames) {
      println(s"Frame $frame: commands '$newCommands'")
      renderToFile(newGameState, s"test$frame.html")
    }

    if (newGameState.hasEnded) {
      //renderToFile(newgame, s"test${i}.html")
      (newCommands, newGameState.score)
    } else {
      go(newGameState, newCommands, verbose, frame + 1)
    }
  }

  def nestedGo(gameState: GameState, commandsAcc: List[Command], commandsProposals: Seq[Seq[Command]]): (GameState, List[Command]) = {
    import gameState._
    assert(!hasEnded)
    assert(currentUnit.isDefined)
    val commandOptions = commandsProposals.head
    val (next, valid) = commandOptions.filter(_.valid).headOption match {
      case None =>
        (Move(SW), false)
      case Some(next) =>
        (next, true)
    }
    viz.step(gameState, next, lock = !valid)
    val newGameState = gameState.move(next, valid)
    val newCommands = commandsAcc :+ next

    if (newGameState.hasEnded || !valid) {
      (newGameState, newCommands)
    } else
      nestedGo(newGameState, newCommands, commandsProposals.tail)
  }
}

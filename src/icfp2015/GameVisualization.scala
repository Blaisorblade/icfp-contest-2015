package icfp2015

import java.io._

object GameVisualization {

  private var fh: PrintWriter = null

  def render(g: GameState) = {
    import g._

    def cells(y: Int): Seq[scala.xml.Node] = for {
      x <- 0 until width
    } yield {
      val c = Cell(x, y)

      var classes = "cell" :: Nil

      if (filled(c)) classes = classes :+ "filled"

      currentUnit.map {
        case GameUnit(cs, pivot, _) =>
          if (cs contains c) classes = classes :+ "unit"
          if (pivot == c) classes = classes :+ "pivot"
      }

      <div class={classes mkString " "}></div>

    }

    // TODO render current unit
    val rows: Seq[scala.xml.Node] = for {
      y <- 0 until height
    } yield <div class="row">{cells(y)}</div>

    // maybe use stylesheet on server to ignore resources copying.

    <div class="board">{rows}</div>
  }

  class Visualizer {
    def problem[T](p: Problem)(block: => T): T = {
      fh = new PrintWriter(debugFile(p.id.toString))
      fh write """<!DOCTYPE html>
      <html>
        <script src="https://code.jquery.com/jquery-2.1.4.js"></script>
        <script src="resources/app.js"></script>
        <head>
          <link rel="stylesheet" href="resources/styles.css"/>
        </head>
        <body>"""

      val res = block
      fh write """</body></html>"""
      fh.close()
      res
    }
    // should internally increment a counter
    def game[T](g: GameState)(block: => T): T = {
      fh write """<div class="game" data-curr=0>
        <nav class="controls">
          <button class="start">&lt;&lt;-</button>
          <button class="prev">&lt;</button>
          <button class="next">&gt;</button>
          <button class="end">-&gt;&gt;</button>
        </nav> """
      val res = block
      fh write """</div>"""
      res
    }
    def step(g: GameState, cmd: Command, lock: Boolean) = {
      fh write """<div class="frame">"""
      fh write render(g).toString
      fh write s"""<span class="cmd">Next command: ${if (lock) "LOCK " else ""}${cmd}. Score: ${g.score.score}</span>"""
      fh write """</div>"""
    }
  }

  private def debugFile(name: String) = new File(s"debug_${name}.html")

  def renderToFile(g: GameState, filePath: String) = {
    val writer = new PrintWriter(new File(filePath))
    writer.write(render(g).toString)
    writer.close()
  }
}

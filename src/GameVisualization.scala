package icfp2015

import java.io._

trait GameVisualization { self: Game =>

  def render(g: GameState) = {
    import g._

    def cells(y: Int): Seq[scala.xml.Node] = for {
      x <- 0 until width
    } yield {
      println(x)
      if (filled(Cell(x, y))) {
        <div class="cell filled"></div>
      } else {
        <div class="cell"></div>
      }
    }

    // TODO render current unit
    val rows: Seq[scala.xml.Node] = for {
      y <- 0 until height
    } yield <div class="row">{cells(y)}</div>

    // maybe use stylesheet on server to ignore resources copying.
    val board =
      <html>
        <head>
          <link rel="stylesheet" href="resources/styles.css"/>
        </head>
        <body>
          <div id="board">{rows}</div>
        </body>
      </html>

    board
  }

  def renderToFile(g: GameState, filePath: String) = {
    val writer = new PrintWriter(new File(filePath))
    writer.write(render(g).toString)
    writer.close()
  }
}

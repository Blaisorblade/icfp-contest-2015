package icfp2015

import java.io._

object GameVisualization {

  def render(g: GameState) = {
    import g._

    def cells(y: Int): Seq[scala.xml.Node] = for {
      x <- 0 until width
    } yield {
      val c = Cell(x, y)

      var classes = "cell" :: Nil

      if (filled(c)) classes = classes :+ "filled"

      currentUnit.map {
        case GameUnit(cs, pivot) =>
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

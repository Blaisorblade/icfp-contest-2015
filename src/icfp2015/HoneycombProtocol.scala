package icfp2015

import spray.json._

object HoneycombProtocol extends DefaultJsonProtocol {
  implicit val cellFormat    = jsonFormat2(Cell)
  implicit val unitFormat    = jsonFormat2(GameUnit)
  implicit val problemFormat = jsonFormat7(Problem)

  implicit val outputFormat  = jsonFormat4(Output)
}

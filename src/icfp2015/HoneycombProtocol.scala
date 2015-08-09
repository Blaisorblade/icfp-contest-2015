package icfp2015

import spray.json._

object HoneycombProtocol extends DefaultJsonProtocol {
  implicit val cellFormat    = jsonFormat2(Cell)

  implicit object unitFormat extends RootJsonFormat[GameUnit] {
    def write(c: GameUnit) = JsObject(
      "members" -> c.members.toJson,
      "pivot" -> c.pivot.toJson
    )

    def read(value: JsValue): GameUnit = value match {
      case JsObject(fields) =>
          GameUnit(fields("members").convertTo[List[Cell]], fields("pivot").convertTo[Cell])
      case _ => throw new DeserializationException("GameUnit expected")
    }
  }

  implicit val problemFormat = jsonFormat7(Problem)

  implicit val outputFormat  = jsonFormat4(Output)
}

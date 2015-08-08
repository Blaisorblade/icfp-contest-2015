package object icfp2015 {

  //Testing status: light testing done.

  def fromHex(x: Int, y: Int): (Int, Int) =
    (x + (y + 1) / 2, y)

  def toHex(x: Int, y: Int): (Int, Int) =
    (x - (y + 1) / 2, y)

}

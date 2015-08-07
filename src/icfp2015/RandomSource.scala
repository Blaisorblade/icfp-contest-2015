package icfp2015
/**
 * The Source
 *
 * The order of the units in the source will be determined using a pseudo-random sequence of numbers, starting with a given seed. The unit identified by a random number is obtained by indexing (starting from 0) into the field units, after computing the modulo of the number and the length of field units. So, for example, if the configuration contains 5 units, then the number 0 will refer to the first unit, while the number 7 will refer the 3rd one (because 7 mod 5 is 2, which refers to the 3rd element).
 *
 * The pseudo-random sequence of numbers will be computed from a given seed using a linear congruential generator with the following parameters:
 * @author pgiarrusso
 */
object RandomSource {
  val modulusBits = 32
  val multiplier = 1103515245
  val increment = 12345
  def newState(state: Int) =
    multiplier * state + increment
  def mask(bits: Int) = (1 << (bits + 1)) - 1
  val highLimit = 30
  val lowLimit = 16
  val stateMask = mask(highLimit) ^ mask(lowLimit - 1)
    //"%x" format stateMask = "7fff0000"
    //(((1 << 31) - 1) ^ ((1 << 16) - 1))
    //0xffffffff //XXX
  def getNum(state: Int) = (state & stateMask) >> lowLimit

  def apply(seed: Int) = new RandomSource(seed)
}

//Purely functional
class RandomSource(val state: Int) {
  import RandomSource._
  def next: (Int, RandomSource) = {
    (getNum(state), new RandomSource(newState(state)))
  }
}

class RandomSourceState(var state: Int) {
  import RandomSource._
  def next: (Int) = {
    val ret = getNum(state)
    state = newState(state)
    ret
  }
}


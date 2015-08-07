/**
 * The Source
 *
 * The order of the units in the source will be determined using a pseudo-random sequence of numbers, starting with a given seed. The unit identified by a random number is obtained by indexing (starting from 0) into the field units, after computing the modulo of the number and the length of field units. So, for example, if the configuration contains 5 units, then the number 0 will refer to the first unit, while the number 7 will refer the 3rd one (because 7 mod 5 is 2, which refers to the 3rd element).
 *
 * The pseudo-random sequence of numbers will be computed from a given seed using a linear congruential generator with the following parameters:
 * @author pgiarrusso
 */
class RandomSource {

  val modulusBits = 32
  val multiplier = 1103515245
  val increment = 12345
}
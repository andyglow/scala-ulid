package ulid

import org.scalatest._
import org.scalatest.Matchers._


class CrockfordSpec extends FunSuite {
  import CrockfordSpec._

  test("encode") {
    val combos = List(
      (0L , 0L , 0L , "00000000000000000000000000"),
      (1L , 0L , 0L , "10000000000000000000000000"),
      (9L , 0L , 0L , "90000000000000000000000000"),
      (10L, 0L , 0L , "A0000000000000000000000000"),
      (31L, 0L , 0L , "Z0000000000000000000000000"),
      (32L, 0L , 0L , "01000000000000000000000000"),
      (63L, 0L , 0L , "Z1000000000000000000000000"),
      (max, 0L , 0L , "ZZZZZZZZZZ0000000000000000"),
      (max, max, 0L , "ZZZZZZZZZZZZZZZZZZ00000000"),
      (max, max, max, "ZZZZZZZZZZZZZZZZZZZZZZZZZZ")
    )

    combos foreach { case (time: Long, rndL: Long, rndM: Long, expected: String) =>
      Crockford.encode(time, fixedRnd(rndL, rndM)) shouldBe expected
    }
  }

  test("isValid") {
    Crockford.isValid("") shouldBe false
    Crockford.isValid("0 000000000000000000000000") shouldBe false
    Crockford.isValid("0.000000000000000000000000") shouldBe false
    Crockford.isValid("0-000000000000000000000000") shouldBe false
    Crockford.isValid("00000000000000000000000000") shouldBe true
    Crockford.isValid("ZZZZZZZZZZZZZZZZZZZZZZZZZZ") shouldBe true
    Crockford.isValid("ZzZZZZZZZZZZZZZZZZZZZZZZZZ") shouldBe false
  }
}

object CrockfordSpec {

  def max: Long = Long.MaxValue

  def fixedRnd(l: Long, m: Long): Rnd = Rnd.make((l, m))
}
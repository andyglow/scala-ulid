package ulid

import org.scalatest._
import matchers.should.Matchers._
import org.scalatest.matchers
import org.scalatest.funsuite.AnyFunSuite


class RndSpec extends AnyFunSuite {

  test("Rnd.lookupDefault") {
    Rnd.lookupDefault(Map()) shouldBe Rnd.fastWeakSeed
    Rnd.lookupDefault(Map("ulid.rnd" -> "zzz")) shouldBe Rnd.fastWeakSeed
    Rnd.lookupDefault(Map("ulid.rnd" -> "weak")) shouldBe Rnd.weak
    Rnd.lookupDefault(Map("ulid.rnd" -> "secure")) shouldBe Rnd.secure
    Rnd.lookupDefault(Map("ulid.rnd" -> "uuid")) shouldBe Rnd.uuid
    Rnd.lookupDefault(Map("ulid.rnd" -> "fastSecureSeed")) shouldBe Rnd.fastSecureSeed
    Rnd.lookupDefault(Map("ulid.rnd" -> "fastWeakSeed")) shouldBe Rnd.fastWeakSeed
  }

  test("Rnd.set produce unique values") {
    def str(rnd: Rnd): String = {
      val buf = Array.ofDim[Char](16)
      rnd.set(buf, 0)
      new String(buf)
    }

    for {
      rnd <- List(Rnd.weak, Rnd.secure, Rnd.uuid, Rnd.fastSecureSeed, Rnd.fastWeakSeed)
      _   <- 0 until 1000
    } {
      val v1 = str(rnd)
      val v2 = str(rnd)

      v1 shouldNot equal(v2)
    }
  }
}

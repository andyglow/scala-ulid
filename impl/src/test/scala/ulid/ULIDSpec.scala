package ulid

import org.scalatest.Matchers._
import org.scalatest._


class ULIDSpec extends FunSuite {

  test("ULID() produce result of type ULID") {
    val id = ULID()
    id.isInstanceOf[ULID] shouldBe true
  }

  test("ULID() produce unique values") {
    for {
      rnd <- List(Rnd.weak, Rnd.secure, Rnd.uuid, Rnd.fastSecureSeed, Rnd.fastWeakSeed)
      _   <- 0 until 1000
    } {
      val v1 = ULID()(rnd)
      val v2 = ULID()(rnd)

      v1 shouldNot equal(v2)
    }
  }
}

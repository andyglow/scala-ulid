package ulid

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.{Random, UUID}
import java.util.concurrent.atomic.AtomicInteger

import scala.annotation.tailrec


trait Rnd extends {

  def set(buf: Array[Char], offset: Int): Unit
}

object Rnd {

  def make(f:  => (Long, Long)): Rnd = new Rnd {

    override def set(buf: Array[Char], offset: Int): Unit = {
      val (less, most) = f

      @tailrec def setChar(i: Int = 0): Unit = if (i < 16) {
        val b = if (i < 8) {
          ((less >> (i * 8)) & 0xFF).toByte
        } else {
          ((most >> ((i - 8) * 8)) & 0xFF).toByte
        }

        buf(offset + i) = Crockford.EncodeChars(b + 128)
        setChar(i + 1)
      }

      setChar()
    }
  }

  lazy val fastWeakSeed: Rnd = {
    val rnd = new Random()
    val nodeId = rnd.nextInt
    val seed = new AtomicInteger(rnd.nextInt)
    make {
      (nodeId << 16 | seed.incrementAndGet, System.nanoTime)
    }
  }

  lazy val fastSecureSeed: Rnd = {
    val rnd = SecureRandom.getInstance("NativePRNGNonBlocking")
    val nodeId = rnd.nextInt
    val seed = new AtomicInteger(rnd.nextInt)
    make {
      (nodeId << 16 | seed.incrementAndGet, System.nanoTime)
    }
  }

  lazy val uuid: Rnd = {
    make {
      val uuid = UUID.randomUUID()

      (uuid.getLeastSignificantBits, uuid.getMostSignificantBits)
    }
  }

  lazy val weak: Rnd = {
    val r = new Random()
    make {
      (r.nextLong(), r.nextLong())
    }
  }

  lazy val secure: Rnd = {
    val r = SecureRandom.getInstance("NativePRNGNonBlocking")
    make {
      (r.nextLong(), r.nextLong())
    }
  }

  lazy val default: Rnd = {
    sys.props.getOrElse("ulid.rnd", "fastWeakSeed") match {
      case "weak"           => weak
      case "secure"         => secure
      case "uuid"           => uuid
      case "fastSecureSeed" => fastSecureSeed
      case "fastWeakSeed"   => fastWeakSeed
      case _                => weak
    }
  }
}

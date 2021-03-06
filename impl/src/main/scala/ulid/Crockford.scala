package ulid

import scala.annotation.tailrec

/** Crockford's Base32
  * http://www.crockford.com/wrmg/base32.html
  */
private[ulid] object Crockford {

  /** 256 chars long array containing repetitive abcs
    */
  private[ulid] val EncodeChars: Array[Char] = {
    @tailrec def appendOrTrim(
      v: Seq[Char] = Seq.empty,
      extra: Seq[Char],
      len: Int): Seq[Char] = if (v.length >= len) v.take(len) else appendOrTrim(v ++ extra, extra, len)

    appendOrTrim(
      extra = Seq('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'),
      len   = 256).toArray
  }

  private val Modus = 32

  private val TimeLen = 10

  private val Len = 26

  def encode(time: => Long, rnd: Rnd): String = {

    @tailrec def doTime(t: Long, idx: Int, f: (Int, Int) => Unit): Unit = if (idx < TimeLen) {
      val mod = (t % Modus).toInt
      f(idx, mod)
      doTime((t - mod) / Modus, idx + 1, f)
    }

    val buf = Array.ofDim[Char](Len)
    doTime(time, 0, (i, c) => buf(i) = EncodeChars(c))
    rnd.set(buf, 10)

    new String(buf)
  }

  def isValid(x: String): Boolean =
    x.length == 26 && x.forall { c =>
      val b = c.toByte
      def isDig = b >= 48 && b <= 57
      def isChr = b >= 65 && b <= 90

      isDig || isChr
    }
}

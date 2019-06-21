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

  private[ulid] val ValidChars: Array[Byte] = Array[Byte](
    -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1,
    0, 1, 2, 3, 4, 5, 6, 7,
    8, 9, -1, -1, -1, -1, -1, -1,
    -1, 10, 11, 12, 13, 14, 15, 16,
    17, 1, 18, 19, 1, 20, 21, 0,
    22, 23, 24, 25, 26, -1, 27, 28,
    29, 30, 31, -1, -1, -1, -1, -1,
    -1, 10, 11, 12, 13, 14, 15, 16,
    17, 1, 18, 19, 1, 20, 21, 0,
    22, 23, 24, 25, 26, -1, 27, 28,
    29, 30, 31)

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
    x.length == 26 && x.forall(ValidChars(_) != -1)
}

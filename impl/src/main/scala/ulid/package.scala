package object ulid {

  object ULID {

    type Repr = String

    type Base = Any { type ULID$TYPE }

    trait Tag extends Any

    type Type <: Base with Tag

    private[ulid] def wrap(x: String): Type = x.asInstanceOf[Type]

    private[ulid] def unwrap(x: Type): String = x.asInstanceOf[String]

    def apply()(implicit rnd: Rnd = Rnd.fastWeakSeed): ULID = wrap(Crockford.encode(System.currentTimeMillis, rnd))

    def unapply(x: String): Option[Type] = if (Crockford.isValid(x)) Some(wrap(x)) else None
  }

  type ULID = ULID.Type

  implicit class ULIDOps(private val ulid: ULID) extends AnyVal {

    def str: String = ULID.unwrap(ulid)
  }
}

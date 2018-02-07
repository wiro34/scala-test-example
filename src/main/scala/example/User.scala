package example

case class Id[T](value: Long) extends AnyVal

object Id {
  def empty[T]: Id[T] = Id[T](0)
}

case class User(id: Id[User], name: String)

package example

import scala.util.Try

trait UserRepository {
  def create(user: User): Try[User]

  def exists(name: String): Try[Boolean]
}

object UserRepositoryImpl extends UserRepository {
  override def create(user: User): Try[User] = {
    Db.execute(s"insert into users set (name) values (${user.name})")
    Db.query(s"select * from users where id = last_inserted_id()")
      .map(_.headOption match {
        case Some(List(id: Long, name: String)) => User(Id(id), name)
        case Some(_) => throw new IllegalStateException("Invalid record")
        case None => throw new NoSuchElementException("Record not found")
      })
  }

  override def exists(name: String): Try[Boolean] = {
    Db.query(s"select count(1) from users where name = ${name}")
      .map(_.nonEmpty)
  }
}

trait UsesUserRepository {
  val userRepository: UserRepository
}

trait MixInUserRepository extends UsesUserRepository {
  override val userRepository: UserRepository = UserRepositoryImpl
}

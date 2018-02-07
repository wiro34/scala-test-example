package example

import scala.util.{Failure, Try}

trait UserCreateService {
  def create(name: String): Try[User]
}

trait UserCreateServiceImpl extends UserCreateService with UsesUserRepository {
  def create(name: String): Try[User] =
    userRepository.exists(name).flatMap {
      case false => userRepository.create(User(Id.empty, name))
      case true => Failure(new RuntimeException("That username already exists"))
    }
}

trait UsesUserCreateService {
  val userCreateService: UserCreateService
}

trait MixInUserCreateService extends UsesUserCreateService {
  override val userCreateService: UserCreateService =
    new UserCreateServiceImpl
      with MixInUserRepository
}

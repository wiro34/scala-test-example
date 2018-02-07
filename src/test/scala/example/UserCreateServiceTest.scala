package example

import java.sql.SQLException

import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.util.{Failure, Success}

class UserCreateServiceTest extends WordSpec
  with Matchers
  with Inside
  with MockFactory {

  val service = new UserCreateServiceImpl {
    override val userRepository: UserRepository = stub[UserRepository]
  }

  "#create" when {
    "user is not existed" should {
      "return Success with a create user" in {
        (service.userRepository.exists _).when(*).returns(Success(false))
        (service.userRepository.create _).when(*).onCall { user: User => Success(User(Id(1), user.name)) }

        inside(service.create("foo")) {
          case Success(user: User) =>
            user.id.value shouldBe 1
            user.name shouldBe "foo"
        }
      }
    }

    "username already exists" should {
      "return Failure" in {
        (service.userRepository.exists _).when(*).returns(Success(true))
        inside(service.create("foo")) {
          case Failure(e) =>
            e.getMessage shouldBe "That username already exists"
        }
      }
    }

    "an unknown exception thrown" should {
      "return Failure" in {
        (service.userRepository.exists _).when(*).returns(Failure(new SQLException("Invalid sql syntax")))
        inside(service.create("foo")) {
          case Failure(e) =>
            e.getMessage shouldBe "Invalid sql syntax"
        }
      }
    }
  }
}

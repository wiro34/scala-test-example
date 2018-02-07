package example

import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}

import scala.util.{Failure, Success}

class ControllerTest extends WordSpec
  with Matchers
  with BeforeAndAfter
  with MockFactory {

  val controller = new Controller with TestableLogging {
    override val userCreateService: UserCreateService = stub[UserCreateService]
  }

  before {
    controller.logger.reset()
  }

  "#action" when {
    "name is not empty" when {
      trait ValidName {
        val name = "foo"
      }

      "creating user is succeeded" should {
        trait WithSuccessService extends ValidName {
          (controller.userCreateService.create _).when(name).returns(Success(User(Id(1), name)))
        }

        "return SuccessResponse" in new WithSuccessService {
          controller.action(name) shouldBe SuccessResponse
        }

        "output success log" in new WithSuccessService {
          controller.action(name)
          controller.logger.infoMessages shouldBe Seq("Succeeded to create a user: User(Id(1),foo)")
          controller.logger.errorMessages shouldBe Nil
        }
      }

      "creating user is failed" should {
        trait WithFailureService extends ValidName {
          (controller.userCreateService.create _).when(name).returns(Failure(new RuntimeException("That username already exists")))
        }

        "return FailureResponse" in new WithFailureService {
          controller.action(name) shouldBe FailureResponse("That username already exists")
        }

        "output error log" in new WithFailureService {
          controller.action(name)
          controller.logger.errorMessages shouldBe Seq("Failed to create a user: cause=That username already exists")
          controller.logger.infoMessages shouldBe Nil
        }
      }
    }

    "name is empty" should {
      trait InvalidName {
        val name = ""
      }

      "return FailureResponse" in new InvalidName {
        controller.action(name) shouldBe FailureResponse("name is empty")
      }
    }
  }
}

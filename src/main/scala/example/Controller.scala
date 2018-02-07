package example

import scala.util.{Failure, Success}

abstract class Response(status: String)

case object SuccessResponse extends Response("Succeeded")

case class FailureResponse(message: String) extends Response("Failed")

class Controller extends MixInUserCreateService with Logging {
  def action(name: String): Response = Option(name)
    .filter(_.length > 0)
    .map(userCreateService.create)
    .map {
      case Success(user) =>
        logger.info(s"Succeeded to create a user: ${user}")
        SuccessResponse
      case Failure(e) =>
        logger.error(s"Failed to create a user: cause=${e.getMessage}")
        FailureResponse(e.getMessage)
    }
    .getOrElse(FailureResponse("name is empty"))
}

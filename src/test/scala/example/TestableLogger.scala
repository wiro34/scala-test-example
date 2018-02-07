package example

import scala.collection.mutable.ArrayBuffer

class TestableLogger extends Logger {
  val infoMessages = new ArrayBuffer[String]
  val errorMessages = new ArrayBuffer[String]

  override def info(message: String): Unit = infoMessages.append(message)

  override def error(message: String): Unit = errorMessages.append(message)

  def reset() {
    infoMessages.clear()
    errorMessages.clear()
  }
}

trait TestableLogging extends Logging {
  override val logger: TestableLogger = new TestableLogger
}

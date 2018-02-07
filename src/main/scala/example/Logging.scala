package example

trait Logger {
  def info(message: String): Unit

  def error(message: String): Unit
}

object DefaultLogger extends Logger {
  override def info(message: String): Unit = Console.println(message)

  override def error(message: String): Unit = Console.err.println(message)
}

trait Logging {
  val logger: Logger = DefaultLogger
}

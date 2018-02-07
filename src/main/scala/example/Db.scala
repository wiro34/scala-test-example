package example

import scala.util.{Success, Try}

object Db {
  type QueryResult = List[List[Any]]

  /**
    * Execute a query.
    *
    * @param sql query
    * @return
    */
  def execute(sql: String): Try[Unit] = Success() // stub

  /**
    * Execute a query, and return results.
    *
    * @param sql query
    * @return result
    */
  def query(sql: String): Try[QueryResult] = Success(Nil) // stub
}

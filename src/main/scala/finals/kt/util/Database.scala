package finals.kt.util

import scalikejdbc._
import finals.kt.model.User
trait Database { // (Teck Min, 2024)
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)

  ConnectionPool.singleton(dbURL, "me", "mine")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession


}
object Database extends Database{ // (Teck Min, 2024)
  def setupDB() = {
    if (!hasDBInitialize)
      User.initializeTable()
  }
  def hasDBInitialize : Boolean = {

    DB getTable "player" match {
      case Some(x) => true
      case None => false
    }

  }
}

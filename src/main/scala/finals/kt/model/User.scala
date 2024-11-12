package finals.kt.model

import scalafx.beans.property.{StringProperty, ObjectProperty}
import finals.kt.util.Database
import scalikejdbc._
import scala.util.Try

class User (userNameS : String, userPointsS : Int) extends Database { // (Teck Min, 2024)
  def this()     = this("", 0)
  var userName  = new StringProperty(userNameS)
  var userPoints   = ObjectProperty[Int](userPointsS)

  def save() : Try[Int] = {
    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>
        sql"""insert into player (userName, userPoints) values (${userName.value}, ${userPoints.value})""".update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session => sql""" update player
				set userPoints   = ${userPoints.value}
				where userName  = ${userName.value}
				""".update.apply()
      })
    }
  }

  def delete() : Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session => sql""" delete from player
				where userName = ${userName.value} and userPoints = ${userPoints.value}
				""".update.apply()
      })
    } else
      throw new Exception("Player not Exists in Database")
  }
  def isExist : Boolean =  {
    DB readOnly { implicit session => sql""" select * from player where
				userName = ${userName.value} and
				userPoints = ${userPoints.value}
			""".map(rs => rs.string("userName")).single.apply()
    } match {
      case Some(_) => true
      case None => false
    }

  }
}
object User extends Database{ // (Teck Min, 2024)
  def apply (userNameS : String, userPointsS : Int) : User = {
    new User(userNameS, userPointsS)
  }

  def initializeTable() = {
    DB autoCommit { implicit session => sql"""
			create table player (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
			  userName varchar(64),
			  userPoints int
			)
			""".execute.apply()
    }
  }

  def getAllPersons : List[User] = {
    DB readOnly { implicit session =>
      sql"select * from player".map(rs => User(rs.string("userName"),
        rs.int("userPoints"))).list.apply()
    }
  }
}

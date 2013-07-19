package si.zitnik.research.interestmining.util

import java.sql.{DriverManager, Connection, ResultSet, PreparedStatement}
import java.util
import java.lang.String
import scala.Predef.String
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/5/13
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
object AnswersAdder {
  val fromDBName = "interestminingL6"
  val toDBName  = "interestminingl61k"
  val connectionUrl: String = "jdbc:mysql://localhost:3306/?user=slavkoz&password=xs"
  val con = DriverManager.getConnection(connectionUrl);



  private def copyAnswersAndMissingUsersIds(parentId: String) {
    val retVal = new util.ArrayList[String]
    //copy answers
    var stmt: PreparedStatement = con.prepareStatement("INSERT IGNORE INTO "+ toDBName +".Posts SELECT * FROM " + fromDBName + ".Posts WHERE parentId = ?;")
    stmt.setString(1, parentId)
    stmt.executeUpdate()

    //get acceptedAnswerId
    stmt = con.prepareStatement("SELECT acceptedAnswerId FROM " + toDBName + ".Posts WHERE id = ?;")
    stmt.setString(1, parentId)
    val rs = stmt.executeQuery()
    rs.next()
    val acceptedAnswerId = rs.getString("acceptedAnswerId")


    stmt = con.prepareStatement("" +
      "INSERT IGNORE INTO "+ toDBName +".Users " +
      "SELECT u.id, u.categories1, u.categories2, u.categories3, u.reputation, u.views, u.upvotes, u.downvotes " +
      "FROM " +
      " " + fromDBName + ".Users u, " +
      " " + fromDBName + ".Posts p " +
      "WHERE " +
      " p.ownerUserId = u.id AND" +
      " p.id = ?;")
    stmt.setString(1, acceptedAnswerId)
    stmt.executeUpdate()
  }

  private def getAllQuestionIds(): util.ArrayList[String] = {
    val retVal = new util.ArrayList[String]
    val stmt: PreparedStatement = con.prepareStatement("SELECT id FROM " + toDBName + ".Posts WHERE postTypeId = 1;")
    val rs: ResultSet = stmt.executeQuery
    while (rs.next) {
      retVal.add(rs.getString("id"))
    }
    return retVal
  }

  def main(args: Array[String]) {
    for (questionId <- getAllQuestionIds()) {
      copyAnswersAndMissingUsersIds(questionId)
    }
  }
}

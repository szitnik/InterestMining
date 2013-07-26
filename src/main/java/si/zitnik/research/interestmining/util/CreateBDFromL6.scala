package si.zitnik.research.interestmining.util

import java.sql.DriverManager
import io.Source
import java.util
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/19/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
object CreateBDFromL6 {
  val connectionUrl = "jdbc:mysql://localhost:3306/?user=slavkoz&password=xs"
  val con = DriverManager.getConnection(connectionUrl)
  var fromDb: String = null
  var toDb: String = null

  def main(args: Array[String]) {
    fromDb = "interestminingL6"
    toDb = "interestminingl6typeall"


    var inputFile = "/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ1dataNoDup.txt"
    var typeDb = 1
    var questionsNum = 10
    var answersNum = 1
    executeTypeCopy(inputFile, typeDb, questionsNum, answersNum)

    inputFile = "/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ2dataNoDup.txt"
    typeDb = 2
    questionsNum = 11 //are only answers instead of questions
    answersNum = 0
    executeTypeCopy(inputFile, typeDb, questionsNum, answersNum)

    inputFile = "/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ3dataNoDup.txt"
    typeDb = 3
    questionsNum = 5
    answersNum = 6
    executeTypeCopy(inputFile, typeDb, questionsNum, answersNum)

    con.close()
  }


  def executeTypeCopy(inputFile: String, typeDb: Int, questionsNum: Int, answersNum: Int) {
    for (line <- Source.fromFile(inputFile).getLines()) {
      if (line.startsWith("\t")) {
        val questions = if (questionsNum > 0) line.split("\t")(2).split(",").map(_.trim).take(questionsNum).toArray else Array[String]()
        val answers = if (answersNum > 0) line.split("\t")(3).split(",").map(_.trim).take(answersNum).toArray else Array[String]()
        val userId = line.split("\t")(1)

        //1. copy user
        copyUser(userId, typeDb)
        //2. copy threads
        val postsIds = new util.HashSet[String]()
        for (questionId <- (questions ++ answers)) {
          postsIds.add(questionId)
          postsIds.addAll(getAllAnswerIdsForQuestion(questionId))
        }
        for (postId <- postsIds) {
          copyPost(postId)
        }

      }
    }
  }

  //HELPER METHODS
  def copyUser(userId: String, typeDb: Int) {
    var stmt = con.prepareStatement("INSERT INTO %s.Users SELECT *, '%s' FROM %s.Users WHERE id = ?;".format(toDb, typeDb, fromDb))
    stmt.setString(1, userId)
    stmt.executeUpdate()
  }

  def copyPost(postId: String) {
    var stmt = con.prepareStatement("INSERT IGNORE INTO %s.Posts SELECT * FROM %s.Posts WHERE id = ?;".format(toDb, fromDb))
    stmt.setString(1, postId)
    stmt.executeUpdate()
  }

  def getAllAnswerIdsForQuestion(questionId: String) = {
    val retVal = new util.ArrayList[String]()
    val stmt = con.prepareStatement("SELECT id FROM " + fromDb + ".Posts WHERE postTypeId = 2 AND parentId = ?;")
    stmt.setString(1, questionId)
    val rs = stmt.executeQuery()
    while (rs.next()) {
      retVal.add(rs.getString("id"))
    }

    retVal
  }

}

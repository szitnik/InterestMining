package si.zitnik.research.interestmining.util

import java.sql.DriverManager
import collection.mutable.ArrayBuffer
import util.Random

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/5/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
object GenerateTrainingTestByCategories {
  val connectionUrl: String = "jdbc:mysql://localhost:3306/interestminingl61k?user=slavkoz&password=xs"
  val con = DriverManager.getConnection(connectionUrl)



  def process() {
    val random = new Random()
    for (category <- getCategories()) {
      //do for questions
      var ids = util.Random.shuffle(getQuestionCategoryIds(category)).map(v => (v, if (random.nextDouble() <= 0.7) 1 else 0))
      update(ids)
      //do for answers
      ids = util.Random.shuffle(getAnswerCategoryIds(category)).map(v => (v, if (random.nextDouble() <= 0.7) 1 else 0))
      update(ids)
    }
  }

  def update(ids: ArrayBuffer[(String, Int)]) {
    for ((id, train) <- ids) {
      val stmt = con.prepareStatement("" +
        "UPDATE Posts SET train = ? WHERE id = ?;")
      stmt.setInt(1, train)
      stmt.setString(2, id)
      stmt.executeUpdate()
    }
  }

  def getAnswerCategoryIds(category: String) = {
    val retVal = new ArrayBuffer[String]()
    //questions define categories

    val stmt = con.prepareStatement("" +
      "SELECT id FROM Posts WHERE postTypeId = 2 AND cat2 = ?;")
    stmt.setString(1, category)
    val rs = stmt.executeQuery()

    while (rs.next()) {
      retVal.append(rs.getString("id"))
    }

    retVal
  }

  def getQuestionCategoryIds(category: String) = {
    val retVal = new ArrayBuffer[String]()
    //questions define categories

    val stmt = con.prepareStatement("" +
      "SELECT id FROM Posts WHERE postTypeId = 1 AND cat2 = ?;")
    stmt.setString(1, category)
    val rs = stmt.executeQuery()

    while (rs.next()) {
      retVal.append(rs.getString("id"))
    }

    retVal
  }

  def getCategories() = {
    val retVal = new ArrayBuffer[String]()
    //questions define categories

    val stmt = con.prepareStatement("" +
      "SELECT DISTINCT(cat2) AS cat2, COUNT(id) AS cnt FROM Posts WHERE postTypeId = 1 GROUP BY cat2 ORDER BY COUNT(id) DESC;")
    val rs = stmt.executeQuery()

    while (rs.next()) {
      retVal.append(rs.getString("cat2"))
    }

    retVal
  }

  def main(args: Array[String]) {
    process()
  }

}

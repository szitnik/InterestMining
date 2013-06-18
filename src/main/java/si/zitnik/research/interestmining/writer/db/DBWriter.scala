package si.zitnik.research.interestmining.writer.db

import java.sql._
import si.zitnik.research.interestmining.model.stackoverflow.User
import si.zitnik.research.interestmining.util.json.JSONObject
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 5/14/13
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
class DBWriter(val dbName: String) extends Logging {
  val connectionUrl = "jdbc:mysql://localhost:3306/" + dbName + "?user=slavkoz&password=xs"
  val con = DriverManager.getConnection(connectionUrl)

  /**
   * val SQL = "INSERT INTO dbo.Users VALUES ('mirko', 'x:0.3;ffff:0.98;fff:0.7')";
   */
  /* using prepared statement instead
  def insert(SQL: String) {
    var stmt: Statement = null

    //INSERT
    stmt = con.createStatement()
    val updatedNo = stmt.executeUpdate(SQL)
    if (updatedNo == 0) {
      System.err.println("There were no rows updated!")
    }

    stmt.close()
  }     */
  def insert(stmt: PreparedStatement) = {
    val updatedNo = stmt.executeUpdate()
    if (updatedNo == 0) {
      System.err.println("There were no rows updated!")
    }
    stmt.close()
    updatedNo
  }

  def insertEvidence(stms: PreparedStatement, postId: String) = {
    con.setAutoCommit(false)
    var retVal = true
    var savePoint: Savepoint = null
    try {
      savePoint = con.setSavepoint()
      insert(stms)

      val rs = con.prepareStatement("SELECT  ident_current('Evidence')").executeQuery()
      rs.next()
      val evidenceId = rs.getInt(1)

      val stms1 = con.prepareStatement("INSERT INTO EvidencePost VALUES (?, ?)")
      stms1.setInt(1, evidenceId)
      stms1.setString(2, postId)
      insert(stms1)
    } catch {
      case e: Exception => if (e.getMessage.contains("FOREIGN KEY constraint")) {
        retVal = false
        con.rollback(savePoint)
      } else {
        e.printStackTrace()
        System.exit(-1)
      }
    }
    con.setAutoCommit(true)
    retVal
  }

  def updateWordToDocFreqs(words: Set[String]) {
    con.setAutoCommit(false)


    var savePoint: Savepoint = null
    try {
      savePoint = con.setSavepoint()

      for (word <- words) {
        val stms1 = con.prepareStatement("INSERT INTO wordToDocFreq(word,counts) VALUES (?,1) ON DUPLICATE KEY UPDATE counts=counts+1;")
        stms1.setString(1, word)
        insert(stms1)
      }

      con.commit()
    } catch {
      case e: Exception => {
        con.rollback(savePoint)
        println(words.mkString("\n"))
        e.printStackTrace()
        System.exit(-1)
      }
    }
    con.setAutoCommit(true)
  }

  def insertIntoSemSim(questionId: String, allWords: Int, termFreqs: String) {
    val stms1 = con.prepareStatement("INSERT INTO semSim(questionId,allWords, termFreqs) VALUES (?,?,?);")
    stms1.setString(1, questionId)
    stms1.setInt(2, allWords)
    stms1.setString(3, termFreqs)
    insert(stms1)
  }

  /**
   * Return categories
   * @param Id
   * @return
   */
  def userExists(Id: String): Option[(String, String, String)] = {
    val stmt = con.prepareStatement("SELECT categories1, categories2, categories3 FROM Users WHERE id LIKE ?")
    stmt.setString(1, Id)

    val rs = stmt.executeQuery()
    if (rs.next()) {
      return Some( (rs.getString(1), rs.getString(2), rs.getString(3)) )
    } else {
      return None
    }
  }


  def insertOrUpdateCategory(user: User) {
    val categories = userExists(user.Id)
    categories match {
      case None => {
        insert(user.toSql())
      }
      case Some((cat1, cat2, cat3)) => {
        def updateCat(oldCategories: String, jsonNewCategories: JSONObject) = {
          val jsonOldCategories = new JSONObject(oldCategories)

          val it = jsonNewCategories.keys()
          while (it.hasNext) {
            val key = it.next().toString
            if (jsonOldCategories.has(key)) {
              jsonOldCategories.put(key, jsonOldCategories.getInt(key) + jsonNewCategories.getInt(key))
            } else {
              jsonOldCategories.put(key, jsonNewCategories.get(key))
            }
          }

          jsonOldCategories.toString
        }



        val stmt = con.prepareStatement("UPDATE Users SET categories1 = ?, categories2 = ?, categories3 = ? WHERE id LIKE ?")
        stmt.setString(1, updateCat(cat1, user.categories1))
        stmt.setString(2, updateCat(cat1, user.categories1))
        stmt.setString(3, updateCat(cat1, user.categories1))
        stmt.setString(4, user.Id)
        stmt.executeUpdate()
      }
    }

  }

  def getAllQuestionIds(start: Int, end: Int) = {
    val stmt = con.prepareStatement("SELECT id, body FROM Posts WHERE postTypeId = 1 ORDER BY id LIMIT ?, ?;")
    stmt.setInt(1, start)
    stmt.setInt(2, end)
    stmt.executeQuery()
  }

  def getAllQuestionIdsNum() = {
    val stmt = con.prepareStatement("SELECT COUNT(*) FROM Posts WHERE postTypeId = 1;")
    val rs = stmt.executeQuery()
    rs.next()
    rs.getInt(1)
  }

  def delete(SQL: String) {
    var stmt: Statement = null
    stmt = con.createStatement()
    stmt.execute(SQL)
    stmt.close()
  }

  def getThreadTextByQuestionId(questionId: String) = {
    val stmt = con.prepareStatement("SELECT group_concat(body) FROM Posts WHERE postTypeId = 2 AND parentId LIKE ? GROUP BY parentId;")
    stmt.setString(1, questionId)

    val rs = stmt.executeQuery()
    if (rs.next()) {
      rs.getString(1)
    } else {
      logger.warn("Question ID '%s' has no answers found!".format(questionId))
      ""
    }
  }

  def commit() {
    //con.commit()
  }

  def close() {
    con.close()
  }
}

object DBWriter {
  private var writer:DBWriter = null;
  private var dbNameC: String = null;

  def create(dbName: String) {
    dbNameC = dbName
    if (writer == null) {
      writer = new DBWriter(dbName)
    }
  }

  def instance() = {
    writer
  }

  def reinit() {
    writer.commit()
    writer.close()
    writer = new DBWriter(dbNameC)
  }
}


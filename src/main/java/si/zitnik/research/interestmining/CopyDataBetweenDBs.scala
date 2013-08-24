package si.zitnik.research.interestmining

import java.sql.DriverManager
import io.Source
import com.typesafe.scalalogging.slf4j.Logging
import collection.mutable.ArrayBuffer
import writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/27/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
object CopyDataBetweenDBs extends Logging {

  def fileFromFullL6To1kL6() {
    val connectionUrl = "jdbc:mysql://localhost:3306/?user=slavkoz&password=xs"
    val con = DriverManager.getConnection(connectionUrl)


    var stmt = con.prepareStatement("DELETE FROM interestminingL61k.Users;")
    stmt.executeUpdate()
    stmt = con.prepareStatement("DELETE FROM interestminingL61k.Posts;")
    stmt.executeUpdate()


    val lines = Source.fromFile("/Users/slavkoz/temp/L61k.txt").getLines()


    for (line <- lines.zipWithIndex) {
      val splitted = line._1.split("\t")
      val uid = splitted(0)

      var stmt = con.prepareStatement("INSERT INTO interestminingL61k.Users SELECT * FROM interestminingL6.Users WHERE id = ?;")
      stmt.setString(1, uid)
      stmt.executeUpdate()

      for (pid <- splitted(2).split(",")) {
        stmt = con.prepareStatement("INSERT INTO interestminingL61k.Posts SELECT * FROM interestminingL6.Posts WHERE id = ?")
        stmt.setString(1, pid)
        stmt.executeUpdate()
      }
      logger.info("Processed %d lines.".format(line._2))
      if (line._2 == 199) {
        con.close()
        System.exit(0)
      }
    }

    con.close()

  }

  def copyFromMySQLToMSSQLForConceptExtraction() {
    val connectionUrlMySQL = "jdbc:mysql://localhost:3306/interestminingl6type2?user=slavkoz&password=xs"
    val conMySQL = DriverManager.getConnection(connectionUrlMySQL)

    val connectionUrlMSSQL = "jdbc:sqlserver://192.168.7.65\\SQLEXPRESS:1433;databaseName=Concept_Extraction;user=sa;password=xs;";
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    val conMSSQL = DriverManager.getConnection(connectionUrlMSSQL)

    val stmt = conMySQL.prepareStatement("SELECT id, CONCAT(title, ' ', body) as txt FROM Posts WHERE postTypeId=1 ORDER BY id ASC;")
    val rs = stmt.executeQuery()

    while (rs.next()) {
      val stmt1 = conMSSQL.prepareStatement("INSERT INTO dbo.Questions (text, wordCount, source) VALUES (?, ?, 'Sample')")
      logger.info("%s - %s".format(rs.getString(1), rs.getString(2)))
      stmt1.setString(1, rs.getString(2))
      stmt1.setInt(2, rs.getString(2).split(" ").size)
      stmt1.executeUpdate()
    }

  }

  def copyFromMSSQLMySQLForConceptExtraction() {
    val connectionUrlMySQL = "jdbc:mysql://localhost:3306/?user=slavkoz&password=xs"
    val conMySQL = DriverManager.getConnection(connectionUrlMySQL)
    val mySQLDb = "interestminingl6typeall"

    val connectionUrlMSSQL = "jdbc:sqlserver://192.168.7.65\\SQLEXPRESS:1433;databaseName=Concept_Extraction;user=sa;password=xs;";
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    val conMSSQL = DriverManager.getConnection(connectionUrlMSSQL)



    //map indexes
    val realQuestionIndexes = ArrayBuffer[Int]()
    val stmt = conMySQL.prepareStatement("SELECT id FROM "+mySQLDb+".Posts WHERE postTypeId = 1 ORDER BY id DESC;")
    val rs = stmt.executeQuery()
    while (rs.next()) {
      realQuestionIndexes += rs.getString(1).toInt
    }

    val copiedQuestionIndexes = ArrayBuffer[Int]()
    val stmt1 = conMSSQL.prepareStatement("SELECT idQuestion FROM dbo.Questions WHERE source = 'L6-typeALL-Questions' ORDER BY idQuestion DESC;")
    val rs1 = stmt1.executeQuery()
    while (rs1.next()) {
      copiedQuestionIndexes += rs1.getInt(1)
    }

    if (realQuestionIndexes.size != copiedQuestionIndexes.size) {
      throw new Exception("Indexes do not match! Real: %d, Copied: %d".format(realQuestionIndexes.size, copiedQuestionIndexes.size))
    }

    //transfer
    val stmt2 = conMSSQL.prepareStatement("SELECT * FROM dbo.Concepts WHERE engine LIKE 'ConceptExtractor-L6-typeAll-Questions';")
    val rs2 = stmt2.executeQuery()

    DBWriter.create(mySQLDb)
    while (rs2.next()) {
      val postId = realQuestionIndexes(copiedQuestionIndexes.indexOf(rs2.getInt("idQuestion")))+""
      val stmt3 = conMySQL.prepareStatement("SELECT ownerUserId FROM " + mySQLDb + ".Posts WHERE id = ?;")
      stmt3.setString(1, postId)
      val rs3 = stmt3.executeQuery()
      rs3.next()
      val userId = rs3.getString(1)

      DBWriter.instance().insertEvidence(
        userId,
        rs2.getString("text"),
        "ConceptExtractor",
        "1.0,1.0,1.0",
        rs2.getDouble("weight"),
        "Question",
        postId
      )
    }

    DBWriter.instance().close()

  }


  def main(args: Array[String]) {
    //fileFromFullL6To1kL6()
    //copyFromMySQLToMSSQLForConceptExtraction()
    copyFromMSSQLMySQLForConceptExtraction()
  }
}

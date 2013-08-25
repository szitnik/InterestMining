package si.zitnik.research.interestmining

import java.sql.{Statement, Connection, DriverManager}
import collection.mutable.ArrayBuffer

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 8/23/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
object MSSQLToMySQL {
  val connectionUrlMSSQL = "jdbc:sqlserver://192.168.7.65\\SQLEXPRESS:1433;databaseName=interestminingl6typeall;user=sa;password=xs;";
  val connectionUrlMySQL = "jdbc:mysql://octonion:3306/interestminingl6typeall?user=interestmining&password=6hGKsfpdC4aCXC75"
  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")

  val smallBatchSize = 10000
  val bigBatchSize = 1000

  var conMySQL = DriverManager.getConnection(connectionUrlMySQL)
  var conMSSQL = DriverManager.getConnection(connectionUrlMSSQL)

  def dbReinit() {
    conMySQL.close()
    conMySQL = DriverManager.getConnection(connectionUrlMySQL)
    //conMSSQL.close()
    //conMSSQL = DriverManager.getConnection(connectionUrlMSSQL)
  }

  def delete(tableName: String) {
    var stmt: Statement = null
    stmt = conMySQL.createStatement()
    stmt.execute("DELETE FROM %s;".format(tableName))
    stmt.close()
  }

  def copyUsers() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.Users;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")

      (1 to 8).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })
      stmt1.setInt(9, rs.getInt(9))

      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copyPosts() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.Posts;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = smallBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(String,Int,String,String,Int,Int,String,String,String,String,Int,Int,Int,String,String,String)]()
      def getValues() = {
          (rs.getString(1),
          rs.getInt(2),
          rs.getString(3),
          rs.getString(4),
          rs.getInt(5),
          rs.getInt(6),
          rs.getString(7),
          rs.getString(8),
          rs.getString(9),
          rs.getString(10),
          rs.getInt(11),
          rs.getInt(12),
          rs.getInt(13),
          rs.getString(14),
          rs.getString(15),
          rs.getString(16))

      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }


      val sql = "INSERT INTO Posts VALUES " + ("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),"*(valuesBuffer.size-1)) + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*16
        stmt1.setString (i+1, values._1)
        stmt1.setInt    (i+2, values._2)
        stmt1.setString (i+3, values._3)
        stmt1.setString (i+4, values._4)
        stmt1.setInt    (i+5, values._5)
        stmt1.setInt    (i+6, values._6)
        stmt1.setString (i+7, values._7)
        stmt1.setString (i+8, values._8)
        stmt1.setString (i+9, values._9)
        stmt1.setString (i+10, values._10)
        stmt1.setInt    (i+11, values._11)
        stmt1.setInt    (i+12, values._12)
        stmt1.setInt    (i+13, values._13)
        stmt1.setString (i+14, values._14)
        stmt1.setString (i+15, values._15)
        stmt1.setString (i+16, values._16)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
        println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def copyEvidence() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.Evidence;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(Int, String, String, String, String, Double, String)]()
      def getValues() = {
        (
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3),
        rs.getString(4),
        rs.getString(5),
        rs.getDouble(6),
        rs.getString(7)
        )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO Evidence VALUES " + ("(?, ?, ?, ?, ?, ?, ?),"*(valuesBuffer.size-1)) + "(?, ?, ?, ?, ?, ?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*7
        stmt1.setInt    (i+1, values._1)
        stmt1.setString (i+2, values._2)
        stmt1.setString (i+3, values._3)
        stmt1.setString (i+4, values._4)
        stmt1.setString (i+5, values._5)
        stmt1.setDouble (i+6, values._6)
        stmt1.setString (i+7, values._7)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def copyEvidencePost() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.EvidencePost;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(Int, String)]()
      def getValues() = {
        (
          rs.getInt(1),
          rs.getString(2)
          )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO EvidencePost VALUES " + ("(?, ?),"*(valuesBuffer.size-1)) + "(?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*2
        stmt1.setInt    (i+1, values._1)
        stmt1.setString (i+2, values._2)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }


  }

  def copySemSim() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.SemSim;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(String, Int, String)]()
      def getValues() = {
        (
          rs.getString(1),
          rs.getInt(2),
          rs.getString(3)
          )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO semSim VALUES " + ("(?, ?, ?),"*(valuesBuffer.size-1)) + "(?, ?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*3
        stmt1.setString (i+1, values._1)
        stmt1.setInt    (i+2, values._2)
        stmt1.setString (i+3, values._3)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def copySemSimTitle() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.SemSimTitle;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(String, Int, String)]()
      def getValues() = {
        (
          rs.getString(1),
          rs.getInt(2),
          rs.getString(3)
          )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO semSimTitle VALUES " + ("(?, ?, ?),"*(valuesBuffer.size-1)) + "(?, ?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*3
        stmt1.setString (i+1, values._1)
        stmt1.setInt    (i+2, values._2)
        stmt1.setString (i+3, values._3)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def copyWordToDocFreq() {

    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.wordToDocFreq;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(String, Int)]()
      def getValues() = {
        (
          rs.getString(1),
          rs.getInt(2)
          )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO wordToDocFreq VALUES " + ("(?, ?),"*(valuesBuffer.size-1)) + "(?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*2
        stmt1.setString (i+1, values._1)
        stmt1.setInt    (i+2, values._2)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def copyWordToDocFreqTitle() {

    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.wordToDocFreqTitle;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(String, Int)]()
      def getValues() = {
        (
          rs.getString(1),
          rs.getInt(2)
          )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO wordToDocFreqTitle VALUES " + ("(?, ?),"*(valuesBuffer.size-1)) + "(?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*2
        stmt1.setString (i+1, values._1)
        stmt1.setInt    (i+2, values._2)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def copyWords() {

    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.words;")
    val rs = stmt.executeQuery()

    var counter = 0
    val batchSize = bigBatchSize
    while (rs.next()) {

      val valuesBuffer = new ArrayBuffer[(Int, String, Int)]()
      def getValues() = {
        (
          rs.getInt(1),
          rs.getString(2),
          rs.getInt(3)
          )
      }
      valuesBuffer.append(getValues())

      var batchCounter = 1
      while (rs.next() && batchCounter < batchSize) {
        valuesBuffer.append(getValues())
        batchCounter += 1
      }



      val sql = "INSERT INTO words VALUES " + ("(?, ?, ?),"*(valuesBuffer.size-1)) + "(?, ?, ?)"
      val stmt1 = conMySQL.prepareStatement(sql)
      valuesBuffer.zipWithIndex.foreach{case (values,idx) => {
        val i = idx*3
        stmt1.setInt (i+1, values._1)
        stmt1.setString    (i+2, values._2)
        stmt1.setInt (i+3, values._3)
      }}

      stmt1.executeUpdate()

      counter += valuesBuffer.size
      println("Processed %d".format(counter))
      dbReinit()
    }

  }

  def main(args: Array[String]) {
    /*
     DELETE FROM Users;
     DELETE FROM Posts;
     DELETE FROM Evidence;
     DELETE FROM EvidencePost;
     DELETE FROM semSim;
     DELETE FROM semSimTitle;
     DELETE FROM wordToDocFreq;
     DELETE FROM wordToDocFreqTitle;
     DELETE FROM words;

     run:
     - scalac ....
     - java -cp ".;mysql-connector-java-5.1.18-bin.jar;sqljdbc4.jar;scala-library.jar" MSSQLToMySQL
     */
    delete("Users")
    delete("Posts")
    delete("Evidence")
    delete("EvidencePost")
    delete("semSim")
    delete("semSimTitle")
    delete("wordToDocFreq")
    delete("wordToDocFreqTitle")
    delete("words")
    println("Doing Users")
    copyUsers()
    dbReinit()
    println("Doing Posts")
    copyPosts()
    dbReinit()
    println("Doing Evidence")
    copyEvidence()
    dbReinit()
    println("Doing EvidencePost")
    copyEvidencePost()
    dbReinit()
    println("Doing SemSim")
    copySemSim()
    dbReinit()
    println("Doing SemSimTitle")
    copySemSimTitle()
    dbReinit()
    println("Doing WordToDocFreq")
    copyWordToDocFreq()
    dbReinit()
    println("Doing WordToDocFreqTitle")
    copyWordToDocFreqTitle()
    dbReinit()
    println("Doing Words")
    copyWords()
    dbReinit()
  }

}

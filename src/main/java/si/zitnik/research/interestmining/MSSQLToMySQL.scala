package si.zitnik.research.interestmining

import java.sql.{Connection, DriverManager}

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

  var conMySQL = DriverManager.getConnection(connectionUrlMySQL)
  var conMSSQL = DriverManager.getConnection(connectionUrlMSSQL)

  def dbReinit() {
    conMySQL.close()
    conMySQL = DriverManager.getConnection(connectionUrlMySQL)
    //conMSSQL.close()
    //conMSSQL = DriverManager.getConnection(connectionUrlMSSQL)
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
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO Posts VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

      (1 to 1).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })
      stmt1.setInt(2, rs.getInt(2))
      (3 to 4).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })
      stmt1.setInt(5, rs.getInt(5))
      stmt1.setInt(6, rs.getInt(6))
      (7 to 10).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })
      stmt1.setInt(11, rs.getInt(11))
      stmt1.setInt(12, rs.getInt(12))
      stmt1.setInt(13, rs.getInt(13))
      (14 to 16).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })



      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copyEvidence() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.Evidence;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO Evidence VALUES (?, ?, ?, ?, ?, ?, ?)")

      stmt1.setInt(1, rs.getInt(1))
      (2 to 5).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })
      stmt1.setDouble(6, rs.getDouble(6))
      (7 to 7).foreach(i => {
        stmt1.setString(i, rs.getString(i))
      })

      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copyEvidencePost() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.EvidencePost;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO EvidencePost VALUES (?, ?)")

      stmt1.setInt(1, rs.getInt(1))
      stmt1.setString(2, rs.getString(2))


      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copySemSim() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.SemSim;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO SemSim VALUES (?, ?, ?)")

      stmt1.setString(1, rs.getString(1))
      stmt1.setInt(2, rs.getInt(2))
      stmt1.setString(3, rs.getString(3))


      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copySemSimTitle() {
    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.SemSimTitle;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO SemSimTitle VALUES (?, ?, ?)")

      stmt1.setString(1, rs.getString(1))
      stmt1.setInt(2, rs.getInt(2))
      stmt1.setString(3, rs.getString(3))


      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copyWordToDocFreq() {

    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.wordToDocFreq;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO wordToDocFreq VALUES (?, ?)")

      stmt1.setString(1, rs.getString(1))
      stmt1.setInt(2, rs.getInt(2))


      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copyWordToDocFreqTitle() {

    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.wordToDocFreqTitle;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO wordToDocFreqTitle VALUES (?, ?)")

      stmt1.setString(1, rs.getString(1))
      stmt1.setInt(2, rs.getInt(2))


      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }

  def copyWords() {

    val stmt = conMSSQL.prepareStatement("SELECT * FROM dbo.words;")
    val rs = stmt.executeQuery()

    var counter = 0
    while (rs.next()) {
      val stmt1 = conMySQL.prepareStatement("INSERT INTO words VALUES (?, ?, ?)")

      stmt1.setInt(1, rs.getInt(1))
      stmt1.setString(2, rs.getString(2))
      stmt1.setInt(3, rs.getInt(3))


      stmt1.executeUpdate()

      counter += 1
      if (counter % 1000 == 0) {
        println("Processed %d".format(counter))
        dbReinit()
      }
    }

  }







  def main(args: Array[String]) {
    println("Doing Users")
    copyUsers()
    println("Doing Posts")
    copyPosts()
    println("Doing Evidence")
    copyEvidence()
    println("Doing EvidencePost")
    copyEvidencePost()
    println("Doing SemSim")
    copySemSim()
    println("Doing SemSimTitle")
    copySemSimTitle()
    println("Doing WordToDocFreq")
    copyWordToDocFreq()
    println("Doing WordToDocFreqTitle")
    copyWordToDocFreqTitle()
    println("Doing Words")
    copyWords()
  }

}

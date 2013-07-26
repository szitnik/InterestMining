package si.zitnik.research.interestmining.util

import java.sql.DriverManager
import io.Source

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/25/13
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
object DatabaseExtractorByType {
  val connectionUrl = "jdbc:mysql://octonion:3306/interestminingL6?user=interestmining&password=6hGKsfpdC4aCXC75"
  val con = DriverManager.getConnection(connectionUrl)




  val categories = Array("Society & Culture", "Food & Drink", "Computers & Internet", "Travel", "Cars & Transportation")
  val DBScripts = Array(
    Source.fromFile("csv/DB_scripts/TYPE1DB.sql").getLines().mkString(" "),
    Source.fromFile("csv/DB_scripts/TYPE2DB.sql").getLines().mkString(" "),
    Source.fromFile("csv/DB_scripts/TYPE3DB.sql").getLines().mkString(" ")
  )


  def main(args: Array[String]) {
    for ((dbScript, idx) <- DBScripts.zipWithIndex) {
      println("DB TYPE %d".format(idx+1))
      for (category <- categories) {
        println("\tCategory data: %s".format(category))
        val stmt = con.prepareStatement(dbScript)
        stmt.setString(1, category)
        stmt.setString(2, category)
        val rs = stmt.executeQuery()
        while (rs.next()) {
          println("\t\t%s\t%s\t%s".format(rs.getString("userId"), rs.getString("questionList"), rs.getString("answerParentIdsList")))
        }
      }
    }
  }
}

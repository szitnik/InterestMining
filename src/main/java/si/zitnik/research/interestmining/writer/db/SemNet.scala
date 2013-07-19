package si.zitnik.research.interestmining.writer.db

import java.sql.{Statement, Savepoint, PreparedStatement, DriverManager}
import si.zitnik.research.interestmining.model.stackoverflow.User
import si.zitnik.research.interestmining.util.json.JSONObject
import si.zitnik.research.interestmining.phases.SemSimResult

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/23/13
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
class SemNet {
  val connectionUrl = "jdbc:mysql://localhost:3306/semnet22?user=slavkoz&password=xs"
  val con = DriverManager.getConnection(connectionUrl)

  def getSemNetTerms(word: String) = {
    val stmt = con.prepareStatement("" +
      "select w1.text, relfreq " +
      "from " +
      "  nouncooccurrences " +
      "    left join words as w1 on relw1 = w1.id " +
      "    left join words as w2 on relw2 = w2.id " +
      "    left join words as w3 on relw3 = w3.id, " +
      "  (select id from words where text = ?) ids " +
      "where " +
      "  termw1 = ids.id and " +
      "  termw2 is null and " +
      "  termw3 is null " +
      "order by " +
      "  relfreq desc " +
      "limit 3; ")
    stmt.setString(1, word)
    stmt.executeQuery()
  }


  def close() {
    con.close()
  }
}


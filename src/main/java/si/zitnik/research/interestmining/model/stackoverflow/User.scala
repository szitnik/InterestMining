package si.zitnik.research.interestmining.model.stackoverflow

import si.zitnik.research.interestmining.writer.db.DBWriter
import collection.immutable.HashMap
import org.json.JSONObject

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
case class User(Id: String,
                Reputation: Int = 0,
                CreationDate: String = "",
                DisplayName: String = "",
                EmailHash: String = "",
                LastAccessDate: String = "",
                WebsiteUrl: String = "",
                Location: String = "",
                Age: Int = 0,
                AboutMe: String = "",
                Views: Int = 0,
                UpVotes: Int = 0,
                DownVotes: Int = 0) {

  //x:0.3;ffff:0.98;fff:0.7
  var categories = new JSONObject()

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO dbo.Users VALUES (?, ?, ?, ?, ?, ?)")
    stmt.setString(1, Id)
    stmt.setString(2, categories.toString())
    stmt.setString(3, Reputation.toString)
    stmt.setString(4, Views.toString)
    stmt.setString(5, UpVotes.toString)
    stmt.setString(6, DownVotes.toString)
    stmt

    /*
    "INSERT INTO dbo.Users VALUES ('%d', '%s', '%s', '%s', '%s', '%s')".format(
      Id,
      categories,
      Reputation,
      Views,
      UpVotes,
      DownVotes
    ) */
  }
}

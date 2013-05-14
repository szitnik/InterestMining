package si.zitnik.research.interestmining.model.stackoverflow

import si.zitnik.research.interestmining.writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
case class User(Id: Int,
                Reputation: Int,
                CreationDate: String,
                DisplayName: String,
                EmailHash: String,
                LastAccessDate: String,
                WebsiteUrl: String,
                Location: String,
                Age: Int,
                AboutMe: String,
                Views: Int,
                UpVotes: Int,
                DownVotes: Int) {

  //x:0.3;ffff:0.98;fff:0.7
  var categories = ""

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO dbo.Users VALUES (?, ?, ?, ?, ?, ?)")
    stmt.setString(1, Id.toString)
    stmt.setString(2, categories)
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

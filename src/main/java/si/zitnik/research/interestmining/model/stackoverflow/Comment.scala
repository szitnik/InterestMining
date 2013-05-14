package si.zitnik.research.interestmining.model.stackoverflow

import si.zitnik.research.interestmining.writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
case class Comment(
                  Id: Int,
                  PostId: Int,
                  Score: String,
                  Text: String,
                  CreationDate: String,
                  UserId: Int) {

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO dbo.Evidence (userId ,keyword ,informationSourceId ,trust ,weight ,typeOfEvidence) VALUES (?,?,?,?,?,?)")
    stmt.setString(1, UserId.toString)
    stmt.setString(2, Text)
    stmt.setString(3, "CommentParser")
    stmt.setString(4, "0.6,0.2,0.2")
    stmt.setDouble(5, 0.7)
    stmt.setString(6, "Comments")
    stmt
  }
}

package si.zitnik.research.interestmining.model.stackoverflow

import si.zitnik.research.interestmining.writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
case class Badge(
                  Id: Int,
                  UserId: Int,
                  Name: String,
                  Date: String) {

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO Evidence (userId ,keyword ,informationSourceId ,trust ,weight ,typeOfEvidence) VALUES (?,?,?,?,?,?)")
    stmt.setString(1, UserId.toString)
    stmt.setString(2, Name)
    stmt.setString(3, "BadgesParser")
    stmt.setString(4, "1,0,0")
    stmt.setDouble(5, 0.9)
    stmt.setString(6, "Badges")
    stmt
  }
}

package si.zitnik.research.interestmining.model.stackoverflow

import si.zitnik.research.interestmining.writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 10:14 PM
 * To change this template use File | Settings | File Templates.
 */
case class Vote(Id: Int,
                PostId: Int,
                VoteTypeId: Int,
                CreationDate: String,
                UserId: Option[Int],
                BountyAmount: Option[Int]) {

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO Evidence (userId ,keyword ,informationSourceId ,trust ,weight ,typeOfEvidence) VALUES (?,?,?,?,?,?)")
    UserId match {
      case Some(x) => stmt.setString(1, x.toString)
      case None => stmt.setString(1, "-1")
    }

    stmt.setString(2, VoteTypeId.toString)
    stmt.setString(3, "VotesParser")
    stmt.setString(4, "0.8,0.1,0.1")
    stmt.setDouble(5, 0.8)
    stmt.setString(6, "Votes")
    stmt
  }
}

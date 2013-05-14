package si.zitnik.research.interestmining.model.stackoverflow

import si.zitnik.research.interestmining.writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
case class Post(
                   Id: Int,
                   PostType: Int,
                   ParentId: Int,
                   AcceptedAnswerId: Int,
                   CreationDate: String,
                   Score: Int,
                   ViewCount: Int,
                   Body: String,
                   OwnerUserId: Int,
                   Title: String,
                   Tags: String,
                   AnswerCount: Int,
                   CommentCount: Int,
                   FavoriteCount: Int) {

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO dbo.Posts VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
    stmt.setString(1, Id.toString)
    stmt.setInt(2, PostType)
    stmt.setString(3, ParentId.toString)
    stmt.setString(4, AcceptedAnswerId.toString)
    stmt.setInt(5, Score)
    stmt.setInt(6, ViewCount)
    stmt.setString(7, Body)
    stmt.setString(8, OwnerUserId.toString)
    stmt.setString(9, Title)
    stmt.setString(10, Tags)
    stmt.setInt(11, AnswerCount)
    stmt.setInt(12, CommentCount)
    stmt.setInt(13, FavoriteCount)
    stmt

    /*
    "INSERT INTO dbo.Posts VALUES ('%d', %d, '%s', '%s', %d, %d, '%s', '%s', '%s', '%s', %d, %d, %d)".format(
      Id,
      PostType,
      ParentId.toString,
      AcceptedAnswerId.toString,
      Score,
      ViewCount,
      Body,
      OwnerUserId,
      Title,
      Tags,
      AnswerCount,
      CommentCount,
      FavoriteCount
    ) */
  }
}

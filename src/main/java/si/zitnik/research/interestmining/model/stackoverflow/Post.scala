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
                   Id: String,
                   PostType: Int,    //1: Question, 2: Answer
                   ParentId: String,
                   AcceptedAnswerId: String,
                   CreationDate: String,
                   Score: Int,
                   ViewCount: Int,
                   Body: String,
                   OwnerUserId: String,
                   Title: String,
                   Tags: String,
                   AnswerCount: Int,
                   CommentCount: Int,
                   FavoriteCount: Int,
                   cat1: String,
                   cat2: String,
                   cat3: String) {

  def toSql() = {
    val stmt = DBWriter.instance().con.prepareStatement("INSERT INTO Posts VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
    stmt.setString(1, Id)
    stmt.setInt(2, PostType)
    stmt.setString(3, ParentId)
    stmt.setString(4, AcceptedAnswerId)
    stmt.setInt(5, Score)
    stmt.setInt(6, ViewCount)
    stmt.setString(7, Body)
    stmt.setString(8, OwnerUserId)
    stmt.setString(9, Title)
    stmt.setString(10, Tags)
    stmt.setInt(11, AnswerCount)
    stmt.setInt(12, CommentCount)
    stmt.setInt(13, FavoriteCount)
    stmt.setString(14, cat1)
    stmt.setString(15, cat2)
    stmt.setString(16, cat3)
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

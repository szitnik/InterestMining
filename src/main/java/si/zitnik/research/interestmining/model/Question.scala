package si.zitnik.research.interestmining.model

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
case class Question(
                   Id: Int,
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
                   FavoriteCount: Int
                   ) {

}

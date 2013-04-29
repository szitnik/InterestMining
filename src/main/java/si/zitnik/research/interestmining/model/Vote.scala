package si.zitnik.research.interestmining.model

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

}

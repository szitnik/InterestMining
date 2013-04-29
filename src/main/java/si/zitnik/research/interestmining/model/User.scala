package si.zitnik.research.interestmining.model

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

}

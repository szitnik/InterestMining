package si.zitnik.research.interestmining

import parse._
import writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
object TransformRunner {

  def main(args: Array[String]) {
    val folder = "/Users/slavkoz/Documents/DR_Research/Datasets/Stack Exchange Data Dump - Sept 2011/Content/092011 Stack Overflow/%s"
    val maxToParse = 1000

    //Evidences are continuously added
    DBWriter.instance().delete("DELETE FROM dbo.EvidencePost")
    DBWriter.instance().delete("DELETE FROM dbo.Evidence")
    DBWriter.instance().delete("DELETE FROM dbo.Users")
    DBWriter.instance().delete("DELETE FROM dbo.Posts")

    println("Doing users...")
    val usersFile = folder.format("users.xml")
    new UsersParser(usersFile).parse(maxToParse)

    println("Doing posts...")
    val postsFile = folder.format("posts.xml")
    new PostsParser(postsFile).parse(maxToParse)

    //Adding only evidences
    val badgesFile = folder.format("badges.xml")
    new BadgesParser(badgesFile).parse(maxToParse)

    val votesFile = folder.format("votes.xml")
    new VotesParser(votesFile).parse(maxToParse)

    val commentsFile = folder.format("comments.xml")
    new CommentsParser(commentsFile).parse(maxToParse)




    DBWriter.instance().close()
  }

}

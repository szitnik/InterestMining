package si.zitnik.research.interestmining

import parse.{QuestionsParser, AnswersParser, BadgesParser, UsersParser}

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

    println("Doing users...")
    val usersFile = folder.format("users.xml")
    new UsersParser(usersFile).parse()

    //val badgesFile = folder.format("badges.xml")
    //val badges = new BadgesParser(badgesFile).parse()


    val postsFile = folder.format("posts.xml")
    println("Doing answers...")
    new AnswersParser(postsFile).parse()
    println("Doing questions...")
    new QuestionsParser(postsFile).parse()
  }

}

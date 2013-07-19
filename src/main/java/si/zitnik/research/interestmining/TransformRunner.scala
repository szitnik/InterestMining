package si.zitnik.research.interestmining

import parse._
import phases.{Phase3, Phase2}
import textprocessors.lemmatizer.Lemmatizer
import stackoverflow._
import writer.db.DBWriter
import yahoo.L6Parser
import collection.mutable.ArrayBuffer
import java.sql.DriverManager
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
object TransformRunner extends Logging {

  def stackoveflow() {
    DBWriter.create("interestmining")

    val folder = "/Users/slavkoz/Documents/DR_Research/Datasets/Stack Exchange Data Dump - Sept 2011/Content/092011 Stack Overflow/%s"
    val maxToParse = Int.MaxValue

    /*
    //PHASE 1: Import
    println("Doing users...")
    DBWriter.instance().delete("DELETE FROM Users")
    val usersFile = folder.format("users.xml")
    new UsersParser(usersFile).parse(maxToParse)
    println("Doing posts...")
    DBWriter.instance().delete("DELETE FROM Posts")
    val postsFile = folder.format("posts.xml")
    new PostsParser(postsFile).parse(maxToParse)
    */

    //PHASE 2: TFIDF/text processing
    DBWriter.instance().delete("DELETE FROM semSim")
    DBWriter.instance().delete("DELETE FROM wordToDocFreq")
    Phase2.process()



    //NOT YET PROCESSED
    /*
    //Adding only evidences
    //Evidences are continuously added
    DBWriter.instance().delete("DELETE FROM EvidencePost")
    DBWriter.instance().delete("DELETE FROM Evidence")

    val badgesFile = folder.format("badges.xml")
    new BadgesParser(badgesFile).parse(maxToParse)

    val votesFile = folder.format("votes.xml")
    new VotesParser(votesFile).parse(maxToParse)

    val commentsFile = folder.format("comments.xml")
    new CommentsParser(commentsFile).parse(maxToParse)
    */

    DBWriter.instance().close()
  }

  def yahooL6() {
    //val filename = "/Users/slavkoz/Documents/DR_Research/Datasets/Webscope_L6/Webscope_L6/FullOct2007.xml"
    //val filename = "/Users/slavkoz/Documents/DR_Research/Datasets/Webscope_L6/Webscope_L6/small_sample.xml"
    //val filename = "/Users/slavkoz/Documents/DR_Research/Datasets/Webscope_L6/Webscope_L6/splitted/split_aa"
    DBWriter.create("interestminingL61k")
    val maxToParse = Int.MaxValue


    //PHASE 1: Data import
    /*logger.info("Phase 1:")
    DBWriter.instance().delete("DELETE FROM Users")
    DBWriter.instance().delete("DELETE FROM Posts")
    new L6Parser(filename).parse(maxToParse)*/


    //PHASE 2: TFIDF/text preprocessing
    logger.info("Phase 2:")
    DBWriter.instance().delete("DELETE FROM semSim")
    DBWriter.instance().delete("DELETE FROM wordToDocFreq")
    DBWriter.instance().delete("DELETE FROM semSimTitle")
    DBWriter.instance().delete("DELETE FROM wordToDocFreqTitle")
    Phase2.process(false)

    //PHASE 3: Evidence generation
    logger.info("Phase 3:")
    //DBWriter.instance().delete("DELETE FROM EvidencePost")
    //DBWriter.instance().delete("DELETE FROM Evidence")
    Phase3.process()


    DBWriter.instance().close()
  }

  def main(args: Array[String]) {
    //stackoveflow()
    yahooL6()
  }

}

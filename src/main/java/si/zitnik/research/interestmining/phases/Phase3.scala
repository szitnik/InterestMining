package si.zitnik.research.interestmining.phases

import si.zitnik.research.interestmining.writer.db.{SemNet, DBWriter}
import com.typesafe.scalalogging.slf4j.Logging
import si.zitnik.research.interestmining.util.json.JSONObject
import scala.collection.JavaConversions._
import si.zitnik.research.interestmining.textprocessors.tfidf.TFIDF
import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/22/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
object Phase3 extends Logging {

  def process() {
    var start = 0
    val batchSize = 1000
    val end = DBWriter.instance().getAllPostsIdsNum()

    logger.info("All questions: %d".format(end))

    while (start <= end) {
      logger.info("Processed: %d/%d".format(start, end))
      val resultSet = DBWriter.instance().getAllPostsIds(start, batchSize)
      processPosts(resultSet, end)


      start += batchSize

      if (start % 10000 == 0) {
        DBWriter.reinit()
      }
    }

  }


  private def processPosts(resultSet: ResultSet, end: Int) {
      while (resultSet.next()) {
        val questionId = resultSet.getString(1)
        //val questionBody = resultSet.getString(2)
        val userId = resultSet.getString(3)
        val cat1 = resultSet.getString("cat1")
        val cat2 = resultSet.getString("cat2")
        val cat3 = resultSet.getString("cat3")
        val typeOfEvidence = if (resultSet.getInt("postTypeId") == 1) {
          "Question"
        } else {
          "Answer"
        }

        //1. insert TFIDF evidences
        val semSimWords = DBWriter.instance().getSemSimRecord(questionId)
        for (wordKey <- semSimWords.termFreqs.keySet()) {
          val word = wordKey.toString

          val tfidf = TFIDF.calculateTFIDF(
            semSimWords.termFreqs.getInt(word),
            semSimWords.allWords,
            end,
            DBWriter.instance().getWordToDocumentFrequency(word)
          )
          DBWriter.instance().insertEvidence(userId, word, "TFIDF", "1.0,1.0,1.0", tfidf, typeOfEvidence, questionId)
        }


        //4. insert TFIDF titles
        val semSimWordsTitle = DBWriter.instance().getSemSimRecordTitle(questionId)
        for (wordKey <- semSimWordsTitle.termFreqs.keySet()) {
          val word = wordKey.toString

          val tfidf = TFIDF.calculateTFIDF(
            semSimWordsTitle.termFreqs.getInt(word),
            semSimWordsTitle.allWords,
            end,
            DBWriter.instance().getWordToDocumentFrequencyTitle(word)
          )
          DBWriter.instance().insertEvidence(userId, word, "TFIDF", "1.0,1.0,1.0", tfidf, "Title", questionId)
        }

        //2. insert SemNet additional values
        val semNet = new SemNet()
        for (wordKey <- semSimWords.termFreqs.keySet()) {
          val word = wordKey.toString

          val rs = semNet.getSemNetTerms(word)
          while (rs.next()) {
            DBWriter.instance().insertEvidence(userId, rs.getString(1), "SemNet", "1.0,1.0,1.0", rs.getDouble(2), typeOfEvidence, questionId)
          }
        }
        semNet.close()

        //3. insert category evidences
        DBWriter.instance().insertEvidence(userId, cat1, "Cat1", "1.0,1.0,1.0", 1.0, typeOfEvidence, questionId)
        DBWriter.instance().insertEvidence(userId, cat2, "Cat2", "1.0,1.0,1.0", 1.0, typeOfEvidence, questionId)
        DBWriter.instance().insertEvidence(userId, cat3, "Cat3", "1.0,1.0,1.0", 1.0, typeOfEvidence, questionId)



        //4. Montilingua
        //5. Antelope
        //USE .NET Source!!!!

      }
  }


}

case class SemSimResult(questionId: String, allWords: Int, termFreqs: JSONObject)

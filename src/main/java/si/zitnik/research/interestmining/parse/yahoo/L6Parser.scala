package si.zitnik.research.interestmining.parse.yahoo

import si.zitnik.research.interestmining.writer.db.DBWriter
import java.io.{FileReader, BufferedReader}
import xml.XML
import si.zitnik.research.interestmining.model.stackoverflow.{User, Post}
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 5/22/13
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
class L6Parser(filename: String) extends Logging {

  def readDocument(reader: BufferedReader): String = {
    var text = ""
    var line = reader.readLine()
    var inside = false
    var end = false

    while (!end && line != null) {
      if (line.startsWith("<vespaadd><document")) {
        inside = true
      }

      if (inside) {
        text += " " + line
      }

      if (line.startsWith("</document></vespaadd>")) {
        inside = false
        end = true
      } else {
        line = reader.readLine()
      }
    }

    text
  }

  def parseDocument(document: String) = {
    val xmlDoc = XML.loadString(document.toString) \\ "document"
    var retVal = 0

    //only if qlang == "en"
    if ((xmlDoc \ "qlang").text.equals("en") && ((xmlDoc \ "nbestanswers") \ "answer_item").size > 5) {
      retVal = 1
      //question (id, subject, content, answercount, id of user having question)
      val question = new Post(
        (xmlDoc \ "uri").text,
        1,
        "",
        (xmlDoc \ "uri").text+"a",
        "",
        0,
        0,
        (xmlDoc \ "content").text,
        (xmlDoc \ "id").text,
        (xmlDoc \ "subject").text,
        "",
        ((xmlDoc \ "nbestanswers") \ "answer_item").size,
        0,
        0,
        (xmlDoc \ "cat").text,
        (xmlDoc \ "maincat").text,
        (xmlDoc \ "subcat").text
      )

      //best answer (id = question_id+"a", content, id of answerer)
      val answer = new Post(
        (xmlDoc \ "uri").text+"a",
        2,
        (xmlDoc \ "uri").text,
        "",
        "",
        0,
        0,
        (xmlDoc \ "bestanswer").text,
        (xmlDoc \ "best_id").text,
        "",
        "",
        0,
        0,
        0,
        (xmlDoc \ "cat").text,
        (xmlDoc \ "maincat").text,
        (xmlDoc \ "subcat").text
      )

      //categories: cat, maincat, subcat
      val questioneer = new User((xmlDoc \ "id").text)
      questioneer.categories1.put((xmlDoc \ "cat").text,1)
      questioneer.categories2.put((xmlDoc \ "maincat").text,1)
      questioneer.categories3.put((xmlDoc \ "subcat").text,1)

      val answerer = new User((xmlDoc \ "best_id").text)
      answerer.categories1.put((xmlDoc \ "cat").text,1)
      answerer.categories2.put((xmlDoc \ "maincat").text,1)
      answerer.categories3.put((xmlDoc \ "subcat").text,1)

      DBWriter.instance().insert(question.toSql())
      DBWriter.instance().insert(answer.toSql())
      DBWriter.instance().insertOrUpdateCategory(questioneer)
      DBWriter.instance().insertOrUpdateCategory(answerer)

      //process other answers
      for (answerNode <- ((xmlDoc \ "nbestanswers") \ "answer_item").zipWithIndex) {
        val answerOther = new Post(
          (xmlDoc \ "uri").text+"a"+answerNode._2,
          2,
          (xmlDoc \ "uri").text,
          "",
          "",
          0,
          0,
          answerNode._1.text,
          "",
          "",
          "",
          0,
          0,
          0,
          (xmlDoc \ "cat").text,
          (xmlDoc \ "maincat").text,
          (xmlDoc \ "subcat").text
        )
        DBWriter.instance().insert(answerOther.toSql())
      }
    }

    retVal
  }

  def parse(maxToParse: Int = Int.MaxValue) {
    val reader = new BufferedReader(new FileReader(filename))
    var questionCounter = 0
    var parsedCounter = 0

    var  curText = readDocument(reader)
    while (curText != null && questionCounter < maxToParse) {
      parsedCounter += parseDocument(curText)
      questionCounter += 1
      if (questionCounter % 1000 == 0) {
        logger.info("Parsed: %d, Inserted to db: %d".format(questionCounter, parsedCounter))
        DBWriter.instance().commit()
        DBWriter.reinit()
      }
      curText = readDocument(reader)
    }
  }
}

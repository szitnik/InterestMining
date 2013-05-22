package si.zitnik.research.interestmining.stackoverflow

import si.zitnik.research.interestmining.model.stackoverflow.{Comment, Badge}
import si.zitnik.research.interestmining.writer.db.DBWriter
import java.io.{FileReader, BufferedReader}
import xml.XML

/**
  * Created with IntelliJ IDEA.
  * User: slavkoz
  * Date: 4/27/13
  * Time: 9:10 PM
  * To change this template use File | Settings | File Templates.
  */
class CommentsParser(filename: String) {

  def parse(maxToParse: Int = Int.MaxValue) {

    val writer = DBWriter.instance()

    val reader = new BufferedReader(new FileReader(filename))
    var line = ""
    var counter = 0

    while (line != null && counter < maxToParse) {
      line = reader.readLine()
      if (line != null && line.toString().contains("<row")) {
        val row = XML.loadString(line.toString)
       val comment = Comment(
         (row \ "@Id").text.toInt,
         (row \ "@PostId").text.toInt,
         (row \ "@Score").text,
         (row \ "@Text").text,
         (row \ "@CreationDate").text,
         if ((row \ "@UserId").text.isEmpty) {-1} else {(row \ "@UserId").text.toInt}
       )
        if (writer.insertEvidence(comment.toSql(), comment.PostId.toString)) {
          counter += 1
          if (counter % 100 == 0) {
            println(counter)
            writer.commit()
          }
        }
      }
    }
  }

}

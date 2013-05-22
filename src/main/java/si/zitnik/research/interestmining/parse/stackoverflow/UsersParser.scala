package si.zitnik.research.interestmining.stackoverflow

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.writer.CSVWriter
import java.io.{FileReader, BufferedReader}
import xml.XML
import si.zitnik.research.interestmining.model.stackoverflow.User
import si.zitnik.research.interestmining.writer.db.DBWriter

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
class UsersParser(filename: String) {

  def parse(maxToParse: Int = Int.MaxValue) {

    val writer = DBWriter.instance()

    val reader = new BufferedReader(new FileReader(filename))
    var line = ""
    var counter = 0

    while (line != null && counter < maxToParse) {
      line = reader.readLine()
      if (line != null && line.toString().contains("<row")) {
        val row = XML.loadString(line.toString)
        val user = User(
          (row \ "@Id").text,
          (row \ "@Reputation").text.toInt,
          (row \ "@CreationDate").text,
          (row \ "@DisplayName").text,
          (row \ "@EmailHash").text,
          (row \ "@LastAccessDate").text,
          (row \ "@WebsiteUrl").text,
          (row \ "@Location").text,
          if ((row \ "@Age").text.isEmpty) {-1} else {(row \ "@Age").text.toInt},
          (row \ "@AboutMe").text,
          (row \ "@Views").text.toInt,
          (row \ "@UpVotes").text.toInt,
          (row \ "@DownVotes").text.toInt
        )
        writer.insert(user.toSql())
        counter += 1
        if (counter % 100 == 0) {
          println(counter)
          writer.commit()
        }
      }
    }
  }

}

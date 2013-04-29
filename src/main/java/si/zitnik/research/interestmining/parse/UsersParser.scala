package si.zitnik.research.interestmining.parse

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.model.User
import si.zitnik.research.interestmining.writer.CSVWriter
import java.io.{FileReader, BufferedReader}
import xml.XML

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/27/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
class UsersParser(filename: String) {

  def parse() {
    val writer = new CSVWriter(
      "csv/users.csv",
      Some("%s\t%s\t%s\t%s".format("Id", "Reputation", "Trust", "Distrust"))
    )
    val reader = new BufferedReader(new FileReader(filename))
    var line = ""
    var counter = 0
    while (line != null) {
      line = reader.readLine()
      if (line != null && line.toString().contains("<row")) {
        val row = XML.loadString(line.toString)
        val user = User(
          (row \ "@Id").text.toInt,
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
        writer.write(Array(
          user.Id.toString.toString,
          user.Reputation.toString,
          1.toString,
          1.toString
        ))
        counter += 1
        if (counter % 100 == 0) {
          println(counter)
        }
      }
    }

    writer.close()
  }

}

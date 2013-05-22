package si.zitnik.research.interestmining.stackoverflow

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.model.stackoverflow.Badge
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
class BadgesParser(filename: String) {

  def parse(maxToParse: Int = Int.MaxValue) {

    val writer = DBWriter.instance()

    val reader = new BufferedReader(new FileReader(filename))
    var line = ""
    var counter = 0

    while (line != null && counter < maxToParse) {
      line = reader.readLine()
      if (line != null && line.toString().contains("<row")) {
        val row = XML.loadString(line.toString)
       val badge = Badge(
         (row \ "@Id").text.toInt,
         (row \ "@UserId").text.toInt,
           (row \ "@Name").text,
             (row \ "@Date").text
       )
        writer.insert(badge.toSql())
        counter += 1
        if (counter % 100 == 0) {
          println(counter)
          writer.commit()
        }
      }
    }
  }

}

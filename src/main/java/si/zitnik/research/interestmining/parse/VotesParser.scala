package si.zitnik.research.interestmining.parse

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.model.stackoverflow.Vote
import si.zitnik.research.interestmining.writer.db.DBWriter
import java.io.{FileReader, BufferedReader}
import xml.XML

/**
  * Created with IntelliJ IDEA.
  * Vote: slavkoz
  * Date: 4/27/13
  * Time: 9:10 PM
  * To change this template use File | Settings | File Templates.
  */
class VotesParser(filename: String) {

   def parse(maxToParse: Int = Int.MaxValue) {

     val writer = DBWriter.instance()

     val reader = new BufferedReader(new FileReader(filename))
     var line = ""
     var counter = 0

     while (line != null && counter < maxToParse) {
       line = reader.readLine()
       if (line != null && line.toString().contains("<row")) {
         val row = XML.loadString(line.toString)
       val vote = Vote(
         (row \ "@Id").text.toInt,
         (row \ "@PostId").text.toInt,
           (row \ "@VoteTypeId").text.toInt,
             (row \ "@CreationDate").text,
               row.attribute("UserId") match {
                 case Some(x) => Some((row \ "@UserId").text.toInt)
                 case None => None
               } ,
                 row.attribute("BountyAmount") match {
                   case Some(x) => Some((row \ "@BountyAmount").text.toInt)
                   case None => None
                 }
       )
       if (writer.insertEvidence(vote.toSql(), vote.PostId.toString)) {
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

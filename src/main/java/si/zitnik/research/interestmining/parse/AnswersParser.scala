package si.zitnik.research.interestmining.parse

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.model.Answer
import si.zitnik.research.interestmining.writer.CSVWriter
import java.io.{FileReader, BufferedReader}
import xml.{XML, Source}

/**
  * Created with IntelliJ IDEA.
  * Answer: slavkoz
  * Date: 4/27/13
  * Time: 9:10 PM
  * To change this template use File | Settings | File Templates.
  */
class AnswersParser(filename: String) {

   def parse() = {
     val AnswerType = 2
     val writer = new CSVWriter(
       "csv/answers.csv",
       Some("%s\t%s\t%s\t%s\t%s".format("Id", "UserId", "ViewCount", "Trust", "Distrust"))
     )
     val reader = new BufferedReader(new FileReader(filename))
     var line = ""
     while (line != null) {
       line = reader.readLine()
       if (line != null && line.toString().contains("<row")) {
         val row = XML.loadString(line.toString)
         if ((row \ "@PostTypeId").text.toInt == AnswerType) {
           val answer = Answer(
             (row \ "@Id").text.toInt,
             (row \ "@ParentId").text.toInt,
             (row \ "@CreationDate").text,
             (row \ "@Score").text.toInt,
             if ((row \ "@ViewCount").text.isEmpty) {-1} else {(row \ "@ViewCount").text.toInt},
             (row \ "@Body").text,
             if ((row \ "@OwnerUserId").text.isEmpty) {-1} else {(row \ "@OwnerUserId").text.toInt},
             (row \ "@Title").text,
             (row \ "@Tags").text,
             if ((row \ "@AnswerCount").text.isEmpty) {-1} else {(row \ "@AnswerCount").text.toInt},
             if ((row \ "@CommentCount").text.isEmpty) {-1} else {(row \ "@CommentCount").text.toInt},
             if ((row \ "@FavoriteCount").text.isEmpty) {-1} else {(row \ "@FavoriteCount").text.toInt}
           )
           writer.write(Array(
             answer.Id.toString,
             answer.OwnerUserId.toString,
             answer.ViewCount.toString,
             1.toString,
             1.toString
           ))
         }
       }
     }

     writer.close()
   }

 }

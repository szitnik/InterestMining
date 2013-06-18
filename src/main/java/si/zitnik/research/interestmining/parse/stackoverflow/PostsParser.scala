package si.zitnik.research.interestmining.stackoverflow

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.writer.CSVWriter
import java.io.{FileReader, BufferedReader}
import xml.{XML, Source}
import si.zitnik.research.interestmining.model.stackoverflow.Post
import si.zitnik.research.interestmining.writer.db.DBWriter

/**
  * Created with IntelliJ IDEA.
  * Post: slavkoz
  * Date: 4/27/13
  * Time: 9:10 PM
  * To change this template use File | Settings | File Templates.
  */
class PostsParser(filename: String) {

   def parse(maxToParse: Int = Int.MaxValue) = {

     val writer = DBWriter.instance()
     val reader = new BufferedReader(new FileReader(filename))
     var line = ""
     var counter = 0


     while (line != null && counter < maxToParse) {
       line = reader.readLine()
       if (line != null && line.toString().contains("<row")) {
         val row = XML.loadString(line)
           val post = Post (
             (row \ "@Id").text,
             (row \ "@PostTypeId").text.toInt,
             if ((row \ "@ParentId").text.isEmpty) {"-1"} else {(row \ "@ParentId").text},
             if ((row \ "@AcceptedAnswerId").text.isEmpty) {"-1"} else {(row \ "@AcceptedAnswerId").text},
             (row \ "@CreationDate").text,
             (row \ "@Score").text.toInt,
             if ((row \ "@ViewCount").text.isEmpty) {-1} else {(row \ "@ViewCount").text.toInt},
             (row \ "@Body").text,
             if ((row \ "@OwnerUserId").text.isEmpty) {"-1"} else {(row \ "@OwnerUserId").text},
             (row \ "@Title").text,
             (row \ "@Tags").text,
             if ((row \ "@AnswerCount").text.isEmpty) {-1} else {(row \ "@AnswerCount").text.toInt},
             if ((row \ "@CommentCount").text.isEmpty) {-1} else {(row \ "@CommentCount").text.toInt},
             if ((row \ "@FavoriteCount").text.isEmpty) {-1} else {(row \ "@FavoriteCount").text.toInt},
             "", "", ""
           )
           writer.insert(post.toSql())
         counter += 1
         if (counter % 100 == 0) {
           println(counter)
           writer.commit()
         }
       }
     }
   }

 }

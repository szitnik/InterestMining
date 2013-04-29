package si.zitnik.research.interestmining.parse

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.model.Vote

/**
  * Created with IntelliJ IDEA.
  * Vote: slavkoz
  * Date: 4/27/13
  * Time: 9:10 PM
  * To change this template use File | Settings | File Templates.
  */
class VotesParser(filename: String) {

   def parse(): ArrayBuffer[Vote] = {
     val retVal = ArrayBuffer[Vote]()

     val file = scala.xml.XML.loadFile(filename)

     for (row <- file \\ "row") {
       retVal += Vote(
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
     }


     retVal
   }

 }

package si.zitnik.research.interestmining.parse

import collection.mutable.ArrayBuffer
import si.zitnik.research.interestmining.model.{Badge, User}

/**
  * Created with IntelliJ IDEA.
  * User: slavkoz
  * Date: 4/27/13
  * Time: 9:10 PM
  * To change this template use File | Settings | File Templates.
  */
class BadgesParser(filename: String) {

   def parse(): ArrayBuffer[Badge] = {
     val retVal = ArrayBuffer[Badge]()

     val file = scala.xml.XML.loadFile(filename)

     for (row <- file \\ "row") {
       retVal += Badge(
         (row \ "@Id").text.toInt,
         (row \ "@UserId").text.toInt,
           (row \ "@Name").text,
             (row \ "@Date").text
       )
     }


     retVal
   }

 }

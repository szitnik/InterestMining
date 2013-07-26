package si.zitnik.research.interestmining.util

import io.Source
import collection.mutable.ArrayBuffer
import collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/19/13
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
object DistinctUsers {
  val orderToCheckForRemoval = Array("Cars & Transportation", "Travel", "Computers & Internet", "Society & Culture", "Food & Drink")


  def getData(file: String) = {
    //                               typex-category, userId, whole line
    val retVal = new mutable.HashMap[String, ArrayBuffer[(String, String)]]()

    var curCategory = ""
    for (line <- Source.fromFile(file).getLines()) {
      if (!line.startsWith("\tu")) {
        //add new category
        curCategory = line
      } else {
        //add new data
        val list = retVal.getOrElseUpdate(curCategory, new ArrayBuffer[(String, String)])
        list.append((line.split("\t")(1), line))
      }
    }

    retVal
  }

  def extractUsers(map: mutable.HashMap[String, ArrayBuffer[(String, String)]]) = {
    map.values.flatten.map(_._1).toArray
  }


  def printResult(map: mutable.HashMap[String, ArrayBuffer[(String, String)]]) {

    for (category <- orderToCheckForRemoval) {
      println(category)
      for ((userId, line) <- map.get(category).get) {
        println(line)
      }
    }
  }

  def performRemoval(distinctUsers: mutable.Set[String], map: mutable.HashMap[String, ArrayBuffer[(String, String)]]) {
    for (category <- orderToCheckForRemoval) {
      val newBuffer = new ArrayBuffer[(String, String)]
      for ((userId, line) <- map.get(category).get) {
        if (distinctUsers.contains(userId)) {
          newBuffer.append((userId, line))
          distinctUsers.remove(userId)
        }
      }
      map.put(category, newBuffer)
    }
  }

  def main(args: Array[String]) {
    var users1 = getData("/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ1data.txt")
    var users2 = getData("/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ2data.txt")
    var users3 = getData("/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ3data.txt")

    val users = new ArrayBuffer[String]()
    users.appendAll(extractUsers(users1))
    users.appendAll(extractUsers(users2))
    users.appendAll(extractUsers(users3))

    val distinctUsers = new mutable.HashSet[String]()
    users.foreach(distinctUsers.add(_))

    //perform removal
    for (usersX <- Array((users3, "TYPEDB3"), (users1, "TYPEDB1"), (users2, "TYPEDB2"))) {
      println(usersX._2)
      performRemoval(distinctUsers, usersX._1)
      printResult(usersX._1)
    }




    println()
  }

}

package si.zitnik.research.interestmining.util

import collection.mutable.ArrayBuffer
import io.Source

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 7/26/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
object DistinctUsersChecker {

  def getUsers(file: String) = {
    Source.fromFile(file).getLines().filter(_.startsWith("\tu")).map(_.split("\t")(1)).toArray
  }

  def main(args: Array[String]) {
    var users1 = getUsers("/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ1dataNoDup.txt")
    var users2 = getUsers("/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ2dataNoDup.txt")
    var users3 = getUsers("/Users/slavkoz/IdeaProjects/ResearchProjects/InterestMining/csv/DB_scripts/typ3dataNoDup.txt")

    val users = new ArrayBuffer[String]()
    users.appendAll(users1)
    users.appendAll(users2)
    users.appendAll(users3)

    println(users.size)
    println(users.toSet.size)

    println(users.map(x=> (x,users.count(y=> y == x))).filter(_._2 > 1).sortBy(_._2).reverse.toSet)


    println()
  }

}

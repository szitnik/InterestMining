package si.zitnik.research.interestmining.writer

import java.io.{FileWriter, BufferedWriter}

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 4/29/13
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
class CSVWriter(
          val filename: String,
          val header: Option[String] = None) {
  val bw = new BufferedWriter(new FileWriter(filename))
  if (header.isDefined) {
    bw.write(header.get + "\n")
  }

  def write(vals: Array[String], separator: String = "\t") {
    bw.write("%s%s".format(vals.mkString(separator), "\n"))
  }

  def write(str: String) {
    bw.write("%s%s".format(str, "\n"))
  }

  def close() {
    bw.close()
  }
}

package si.zitnik.research.interestmining.textprocessors.lemmatizer

import si.zitnik.research.lemmagen.LemmagenFactory
import collection.mutable.ArrayBuffer

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/5/13
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
object Lemmatizer {
  val lm = LemmagenFactory.instance("data/lemmagenENModel.obj")

  def lemmatize(buff: ArrayBuffer[String]) = buff.map(lm.Lemmatize(_)).toBuffer

}

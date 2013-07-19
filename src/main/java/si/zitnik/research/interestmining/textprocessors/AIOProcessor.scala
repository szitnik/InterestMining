package si.zitnik.research.interestmining.textprocessors

import lemmatizer.Lemmatizer
import stopwords.Stopwords
import tokenizer.Tokenizer

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/5/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
object AIOProcessor {
  def process(text: String) = {
    Tokenizer.tokenizer.tokenize(text).
      filter(v => v.toCharArray.filter(!_.isLetter).size == 0).
      filter(!Stopwords.stopwords.contains(_)).
      map(v => Lemmatizer.lm.Lemmatize(v.toLowerCase)).
      filter(v => v.size > 1 && v.size <= 50).
      toBuffer
  }
}

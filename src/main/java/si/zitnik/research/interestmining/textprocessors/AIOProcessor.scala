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
      filter(!Stopwords.stopwords.contains(_)).
      map(Lemmatizer.lm.Lemmatize(_)).
      filter(v => v.size > 1 && v.size <= 50).
      toBuffer
  }
}

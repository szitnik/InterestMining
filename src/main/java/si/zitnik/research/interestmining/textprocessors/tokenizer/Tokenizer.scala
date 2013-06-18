package si.zitnik.research.interestmining.textprocessors.tokenizer

import opennlp.tools.tokenize.{TokenizerModel, TokenizerME}

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/5/13
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
object Tokenizer {
  private val model = this.getClass.getClassLoader.getResourceAsStream("apache-opennlp-1.5.2-incubating/models/en-token.bin")
  private val tokenModel = new TokenizerModel(model)
  model.close()
  val tokenizer = new TokenizerME(tokenModel)

  def tokenize(text: String) = tokenizer.tokenize(text).toBuffer
}

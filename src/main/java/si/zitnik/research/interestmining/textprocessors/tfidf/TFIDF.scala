package si.zitnik.research.interestmining.textprocessors.tfidf

import collection.mutable.ArrayBuffer
import collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: slavkoz
 * Date: 6/5/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
class TFIDF(documents: ArrayBuffer[ArrayBuffer[String]]) {

  private val n = documents.size //number of documents
  //private val wordToIndex = documents.toSet[String].zipWithIndex.toMap //index of words within word vector

  private val wordToDocumentFrequencies = mutable.HashMap[String, Int]()
    //fill wordToDocumentFrequencies
    documents.foreach(document => {
      document.toSet[String].foreach(word => {
        wordToDocumentFrequencies.put(word, wordToDocumentFrequencies.getOrElse(word, 0)+1)
      })
    })

  /*
  def calculateWeightVector(document: ArrayBuffer[String]): SparseVector[Double] = {
    val retVal = SparseVector.zeros[Double](wordToIndex.keySet.size)

    val words = document

    if (words.size != 0 && !document.mkString("").trim.isEmpty) {
      val counts = words.groupBy(x=>x).mapValues(x=>x.length)
      val maxWordCountInDoc = counts.values.max

      words.foreach(word => {
        val tf = counts(word)*1.0/maxWordCountInDoc
        val idf = math.log(n*1.0/wordToDocumentFrequencies(word))
        retVal(wordToIndex(word)) = tf*idf
      })
    }

    retVal
  }
  */

  def calculateTFIDFWeights(words: ArrayBuffer[String]): ArrayBuffer[Double] = {
    val retVal = ArrayBuffer[Double]()


    if (words.size != 0 && !words.mkString("").trim.isEmpty) {
      val counts = words.groupBy(x=>x).mapValues(x=>x.length)
      val maxWordCountInDoc = counts.values.max

      words.zipWithIndex.foreach{case (word, idx) => {
        val tf = counts(word)*1.0/maxWordCountInDoc
        val idf = math.log(n*1.0/wordToDocumentFrequencies(word))
        retVal(idx) = tf*idf
      }}
    }

    retVal
  }
}

object TFIDF {

  def calculateTFIDF(wordCount: Double, allWordsinDocNum: Double, numberOfDocuments: Double, wordToDocumentFrequency: Double) = {
    val tf = wordCount/allWordsinDocNum
    val idf = math.log(numberOfDocuments/wordToDocumentFrequency)
    tf*idf
  }

}

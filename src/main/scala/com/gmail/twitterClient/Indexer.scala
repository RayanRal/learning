package com.gmail.twitterClient

import java.io.File

import org.apache.lucene.document.{Field, Document}
import org.apache.lucene.index._
import org.apache.lucene.morphology.english.EnglishAnalyzer
import org.apache.lucene.morphology.russian.RussianAnalyzer
import org.apache.lucene.store.{FSDirectory, Directory}
import twitter4j.Status

/**
  * Created by rayanral on 11/26/15.
  */
class Indexer(indexDir: String) {
  val dir: Directory = FSDirectory.open(new File(indexDir).toPath)
//  val analyzer = new RussianAnalyzer
  val analyzer = new EnglishAnalyzer
  val config: IndexWriterConfig = new IndexWriterConfig(analyzer)
  val writer = new IndexWriter(dir, config)

  def close() {
    writer.close()
  }

  def indexTweet(tweet: Status) {
    println(s"indexing ${tweet.getId}")
    val doc = new Document
    doc.add(new Field("id", tweet.getId.toString, Field.Store.YES, Field.Index.NOT_ANALYZED))
    doc.add(new Field("author", tweet.getUser.getName, Field.Store.YES, Field.Index.NOT_ANALYZED))
    doc.add(new Field("text", tweet.getText, Field.Store.YES, Field.Index.ANALYZED))

    writer.addDocument(doc)
    System.out.println("indexed " + writer.numDocs)
  }


  def getStats(): Unit = {
    val indexReader = DirectoryReader.open(dir)
    val termVectors = for (i <- 0 to indexReader.maxDoc()) yield indexReader.getTermVectors(i).terms("text")
    termVectors
  }

//    reader.
//      val terms = reader.term()
//      while (terms.next()) {
//        Term term = terms.term();
//        String termText = term.text();
//        int frequency = reader.docFreq(term);
//        frequencyMap.put(termText, frequency);
//        termlist.add(termText);
//      }
//      reader.close();
//      // sort the term map by frequency descending
//      Collections.sort(termlist, new ReverseComparator<String>(
//        new ByValueComparator<String,Integer>(frequencyMap)));
//      // retrieve the top terms based on topTermCutoff
//      List<String> topTerms = new ArrayList<String>();
//      float topFreq = -1.0F;
//      for (String term : termlist) {
//        if (topFreq < 0.0F) {
//          // first term, capture the value
//          topFreq = (float) frequencyMap.get(term);
//          topTerms.add(term);
//        } else {
//          // not the first term, compute the ratio and discard if below
//          // topTermCutoff score
//          float ratio = (float) ((float) frequencyMap.get(term) / topFreq);
//          if (ratio >= topTermCutoff) {
//            topTerms.add(term);
//          } else {
//            break;
//          }
//        }
//      }
//      StringBuilder termBuf = new StringBuilder();
//      BooleanQuery q = new BooleanQuery();
//      for (String topTerm : topTerms) {
//        termBuf.append(topTerm).
//          append("(").
//          append(frequencyMap.get(topTerm)).
//          append(");");
//        q.add(new TermQuery(new Term("text", topTerm)), Occur.SHOULD);
//      }
//      System.out.println(">>> top terms: " + termBuf.toString());
//      System.out.println(">>> query: " + q.toString());
//      return q;
//  }





}

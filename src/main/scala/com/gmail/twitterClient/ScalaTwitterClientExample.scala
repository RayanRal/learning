package com.gmail.twitterClient

import java.util.Date

import org.joda.time.DateTime
import twitter4j.{StatusUpdate, Status, Query, TwitterFactory}
import twitter4j.conf._
import scala.collection.JavaConversions._

/**
  * Created by rayanral on 11/25/15.
  */
object ScalaTwitterClientExample {

  val indexer = new Indexer("data/tweetsEnStatistic")

  def main(args : Array[String]) {
    indexer.getStats()
  }

  def index = {
    // (1) config work to create a twitter object
  val cb = new ConfigurationBuilder()
      .setDebugEnabled(true)
      .setOAuthConsumerKey("XWjj85X2NNCcvEBmTL9Oyooj8")
      .setOAuthConsumerSecret("PPAWuc3Z9EhfnO3LE5i2Dql1ttj7wTv1yJwLdtaoD9CyPs33pp")
      .setOAuthAccessToken("98723802-mBCapmb6Pl502Ch93UlxOgQY6vPZl5zUSSg4PaQ7G")
      .setOAuthAccessTokenSecret("4ZiDgc0BRXJFw6WWWjhalaw3EB7LelKhkYmKQdUNendd6")

    val tf = new TwitterFactory(cb.build())
    val twitter = tf.getInstance()

    //    val query = new Query("#ИдеяДляСтартапа")
    val query = new Query("#startupidea")
    query.setSince(DateTime.now().minusDays(7).toString("YYYY-MM-dd"))
    query.setCount(10)
    var prevLastTweetId: Long = 0
    var lastTweetId: Long = 0
    do {
      val tweets = twitter.search(query).getTweets.toList
      indexTweets(tweets)
      Thread.sleep(1000)

      prevLastTweetId = lastTweetId
      lastTweetId = tweets.map(_.getId).min
      println(s"lastTweetId: $lastTweetId")
      query.setMaxId(lastTweetId - 1)
    } while (prevLastTweetId != lastTweetId)
    indexer.close()
  }

  def indexTweets(tweets: List[Status]) = {
    tweets.filterNot(_.isRetweet).foreach(indexer.indexTweet)
  }

}

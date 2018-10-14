package org.tweet.akka

import java.util.concurrent.atomic.AtomicInteger

import org.tweet.akka.TweetSource.Tweet

trait TweetContentGenerator {

  private val counter = new AtomicInteger(0)

  def generateTweet() = Tweet(s"Tweet with id ${counter.incrementAndGet()}")

}
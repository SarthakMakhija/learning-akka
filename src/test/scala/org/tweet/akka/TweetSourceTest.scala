package org.tweet.akka

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import org.scalatest.WordSpecLike
import org.tweet.akka.TweetSource.{GenerateTweet, Tweet}

class TweetSourceTest extends TestKit(ActorSystem("TestSystem")) with WordSpecLike {

  "TweetSource" should {
    "generate send Tweet To A Registered listener" in {

      val actorRef    = TestActorRef[TweetSource](Props(TestTweetSource.tweetSource))
      val tweetSource = actorRef.underlyingActor

      tweetSource.registerListener(testActor)

      actorRef ! GenerateTweet
      expectMsg(Tweet(content = "test"))
    }

    "generate send Tweet To Multiple Registered listeners" in {

      val testProbe   = TestProbe()
      val actorRef    = TestActorRef[TweetSource](Props(TestTweetSource.tweetSource))
      val tweetSource = actorRef.underlyingActor

      tweetSource.registerListener(testProbe.ref)
      tweetSource.registerListener(testProbe.ref)

      actorRef ! GenerateTweet
      testProbe.expectMsg(Tweet(content = "test"))
    }
  }
}

object TestTweetSource {

  def tweetSource = new TweetSource() with TestTweetContentGenerator

}

trait TestTweetContentGenerator extends TweetContentGenerator {

  override def generateTweet(): Tweet = Tweet(content = "test")

}
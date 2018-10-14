package org.tweet.akka

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef}
import org.tweet.akka.TweetSource.GenerateTweet

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

class TweetSource extends Actor {

  this: TweetContentGenerator =>

  private implicit val ec: ExecutionContext = context.system.dispatcher

  private val tick = context.system.scheduler.schedule(initialDelay = FiniteDuration(100, TimeUnit.MILLISECONDS),
                                                       interval     = FiniteDuration(100, TimeUnit.MILLISECONDS),
                                                       receiver     = self,
                                                       message      = GenerateTweet)

  private val listeners = new ListBuffer[ActorRef]()

  override def receive: Receive = {
    case GenerateTweet =>
      listeners foreach { actor => actor ! generateTweet() }
  }

  def registerListener(listener: ActorRef): Unit = {
    listeners += listener
  }

  override def postStop(): Unit = {
    tick.cancel()
  }

}


object TweetSource {

  case object GenerateTweet

  case class Tweet(content: String)

  def apply: TweetSource = new TweetSource() with TweetContentGenerator
}
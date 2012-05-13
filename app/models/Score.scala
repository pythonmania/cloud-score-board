package models

import redis.clients.jedis._
import scala.collection.JavaConversions._
import scala.collection.immutable.SortedSet
import java.net.URI

case class Score(userid: String, score: Long)

object Score {
  val redisURI = new URI(System.getenv("REDISTOGO_URL"));
  val pool = new JedisPool(new JedisPoolConfig(),
    redisURI.getHost(),
    redisURI.getPort(),
    Protocol.DEFAULT_TIMEOUT,
    redisURI.getUserInfo().split(":", 2)(1));

  def create(score: Score) {
    val jedis = pool.getResource()
    jedis.zadd("highscore", score.score, score.userid)
    pool.returnResource(jedis)
  }

  def all(): Seq[Score] = {
    val jedis = pool.getResource()
    val scoresTuple = jedis.zrevrangeWithScores("highscore", 0, 10).toSeq
    pool.returnResource(jedis)

    scoresTuple map (tuple => {
      Score(tuple.getElement(), tuple.getScore().toLong)
    })
  }
}

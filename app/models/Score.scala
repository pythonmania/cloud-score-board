package models

import redis.clients.jedis._
import scala.collection.JavaConversions._
import scala.collection.immutable.SortedSet

case class Score(userid: String, score: Long)

object Score {
  val pool = new JedisPool(new JedisPoolConfig(), "localhost");

  def create(score: Score) {
    val jedis = pool.getResource()
    jedis.zadd("highscore", score.score, score.userid)
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

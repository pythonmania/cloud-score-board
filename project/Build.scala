import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "US Cloud Score Board"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "redis.clients" % "jedis" % "2.0.0")

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings()

}

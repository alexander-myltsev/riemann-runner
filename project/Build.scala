import sbt._
import Keys._
import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.{ MultiJvm }

object Build extends Build {

  lazy val buildSettings = Defaults.defaultSettings ++ multiJvmSettings ++ Seq(
    organization := "com.myltsev",
    version      := "0.1",
    scalaVersion := "2.10.2",
    // make sure that the artifacts don't have the scala version in the name
    crossPaths   := false
  )

  lazy val example = Project(
    id = "riemann-runner",
    base = file("."),
    settings = buildSettings ++
      Seq(libraryDependencies ++= Dependencies.example)
  ) configs(MultiJvm)

  lazy val multiJvmSettings = SbtMultiJvm.multiJvmSettings ++ Seq(
    compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
    parallelExecution in Test := false,
    resolvers ++= Seq(
      "clojars" at "https://clojars.org/repo/",
      "clojure-utils" at "https://github.com/mikera/clojure-utils/releases"
    ),
    executeTests in Test <<=
      ((executeTests in Test), (executeTests in MultiJvm)) map {
        case ((_, testResults), (_, multiJvmResults))  =>
          val results = testResults ++ multiJvmResults
          (Tests.overall(results.values), results)
      }
  )

  object Dependencies {
    val example = Seq(
      // ---- application dependencies ----
      "com.typesafe.akka"  %% "akka-actor" % "2.2.3",
      "com.typesafe.akka"  %% "akka-remote" % "2.2.3",
      "riemann"              % "riemann"               % "0.2.4",
      "net.mikera"           % "clojure-utils"         % "0.6.0",
      "com.aphyr"            % "riemann-java-client"   % "0.2.8" excludeAll (
        ExclusionRule(organization = "com.yammer.metrics")),

      // ---- test dependencies ----
      "com.typesafe.akka" %% "akka-testkit" % "2.2.3" % "test",
      "org.scalatest"     %% "scalatest" % "2.0" % "test"
    )
  }
}

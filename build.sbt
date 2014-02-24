name := "riemann-runner"

resolvers ++= Seq(
  "collective" at "http://nexus.collective-media.net/content/repositories/thirdparty/",
  "clojars" at "https://clojars.org/repo/"
)

libraryDependencies ++= Seq(
  "riemann"              % "riemann"               % "0.2.5-SNAPSHOT",
  "com.aphyr"            % "riemann-java-client"   % "0.2.8" excludeAll (
  ExclusionRule(organization = "com.yammer.metrics"))
)

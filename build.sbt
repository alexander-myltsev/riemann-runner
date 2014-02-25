name := "riemann-runner"

resolvers ++= Seq(
  "clojars" at "https://clojars.org/repo/"
)

libraryDependencies ++= Seq(
  "riemann"              % "riemann"               % "0.2.4",
  "com.aphyr"            % "riemann-java-client"   % "0.2.8" excludeAll (
    ExclusionRule(organization = "com.yammer.metrics"))
)

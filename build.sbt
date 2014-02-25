name := "riemann-runner"

resolvers ++= Seq(
  "clojars" at "https://clojars.org/repo/",
  "clojure-utils" at "https://github.com/mikera/clojure-utils/releases"
)

libraryDependencies ++= Seq(
  "riemann"              % "riemann"               % "0.2.4",
  "net.mikera"           % "clojure-utils"         % "0.6.0",
  "com.aphyr"            % "riemann-java-client"   % "0.2.8" excludeAll (
    ExclusionRule(organization = "com.yammer.metrics"))
)

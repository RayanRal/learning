name:="AkkaInvestigation"

version:="0.1"

scalaVersion:="2.11.4"

resolvers+= "TypesafeRepository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers := ("bintray-akuznetsov-russianmorphology" at "http://dl.bintray.com/akuznetsov/russianmorphology") +: resolvers.value


resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

val akkaStreamV = "1.0-RC2"


libraryDependencies ++= Seq (
  "org.scalatest" % "scalatest_2.11" % "2.2.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-agent" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M3",
  "joda-time"            % "joda-time"           % "2.4",
//  "com.typesafe.akka" %% "akka-http-core-experimental"          % akkaStreamV,
//  "com.typesafe.akka" %% "akka-http-scala-experimental"         % akkaStreamV,
//  "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaStreamV,
//  "com.typesafe.akka" %% "akka-http-testkit-scala-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-remote" % "2.4-SNAPSHOT",
  "com.typesafe.akka" %% "akka-cluster" % "2.4-SNAPSHOT",
  "org.parboiled" %% "parboiled" % "2.1.0",
  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.twitter4j" % "twitter4j-core" % "4.0.4",
  "org.typelevel" %% "cats" % "0.6.0",

//  lucene
  "org.apache.lucene" % "lucene-core" % "5.1.0",
  "org.apache.lucene" % "lucene-analyzers-common" % "5.1.0",
  "org.apache.lucene" % "lucene-queryparser" % "5.1.0",
  "org.apache.lucene.morphology" % "russian" % "1.1",
  "org.apache.lucene.morphology" % "english" % "1.1",
  "org.apache.lucene.morphology" % "morph" % "1.1"
)
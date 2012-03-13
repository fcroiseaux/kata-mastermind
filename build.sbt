name := "masterming-kata"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.8" % "test",
    "com.typesafe.akka" % "akka-actor" % "2.0"
)


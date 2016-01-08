name := """eswitch"""

version := "1.0"

scalaVersion := "2.11.5"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-remote" % "2.3.9",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.sorm-framework" % "sorm" % "0.3.18",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2",
  "com.typesafe.scala-logging" % "scala-logging-api_2.11" % "2.1.2",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.joda" % "joda-convert" % "1.7",
  "com.github.nikita-volkov" % "embrace" % "0.1.4",
  "com.github.nikita-volkov" % "sext" % "0.2.4",
  "joda-time" % "joda-time" % "2.8.1",
  "com.mchange" % "c3p0" % "0.9.5.1",
  "com.google.guava" % "guava" % "18.0")

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value


name := """ISwearBox"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.pac4j" % "play-pac4j_java" % "1.3.0",
  "org.pac4j" % "pac4j-oauth" % "1.6.0",
  "com.google.inject" % "guice" % "4.0-beta",
  "com.cloudinary" % "cloudinary" % "1.0.14",
  "com.cloudinary" % "cloudinary-taglib" % "1.0.14",
  "uk.co.panaxiom" % "play-jongo_2.11" % "0.7.1-jongo1.0"
)

resolvers ++= Seq(
    "Pablo repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/"
)

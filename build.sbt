name := """ISwearBox"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.6.Final",
  "org.pac4j" % "play-pac4j_java" % "1.3.0",
  "org.pac4j" % "pac4j-oauth" % "1.6.0",
  "org.webjars" % "angularjs" % "1.3.2"
)

resolvers ++= Seq(
    "Pablo repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/"
)

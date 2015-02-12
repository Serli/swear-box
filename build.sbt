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
  "org.hibernate" % "hibernate-entitymanager" % "4.2.8.Final",
  "org.pac4j" % "play-pac4j_java" % "1.3.0",
  "org.pac4j" % "pac4j-oauth" % "1.6.0",
  "org.webjars" % "angularjs" % "1.3.2",
  "com.google.inject" % "guice" % "4.0-beta",
  "com.cloudinary" % "cloudinary" % "1.0.14",
  "com.cloudinary" % "cloudinary-taglib" % "1.0.14",
  "net.vz.mongodb.jackson" % "play-mongo-jackson-mapper_2.10" % "1.1.0",
  "org.mongodb" % "mongo-java-driver" % "2.8.0"
)

resolvers ++= Seq(
    "Pablo repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/"
)

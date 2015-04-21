name := """play-template"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  cache,
  ws,
  filters,
  "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
  "com.github.tuxBurner" %% "play-twbs3" % "1.0",
  "com.github.tuxBurner" %% "play-neo4jplugin" % "1.4.2",
  "com.github.tuxBurner" %% "play-jsannotations" % "1.2.2",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "jquery" % "1.11.2",
  "org.webjars" % "font-awesome" % "4.3.0",
  "org.imgscalr" % "imgscalr-lib" % "4.2",
  "commons-collections" % "commons-collections" % "3.2.1"
)

resolvers ++= Seq(
  "tuxburner.github.io" at "http://tuxburner.github.io/repo",
  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
  "Neo4j" at "http://m2.neo4j.org/content/repositories/releases/",
  Resolver.sonatypeRepo("snapshots")
)

name := """shuttle-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"
PlayKeys.externalizeResources := false
libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.2.2.Final",
  "dom4j" % "dom4j" % "1.6.1" //for hibernate
)


fork in run := false
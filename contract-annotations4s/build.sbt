name := "contract-annotations4s"
organization := "com.monadiccloud.bindingz"
version := "1.0-SNAPSHOT"

crossScalaVersions := Seq("2.10.7", "2.11.11", "2.12.8", "2.13.0-M5")

resolvers ++= Seq(
  "Maven repo1" at "http://repo1.maven.org/",
  "Local Maven" at "/Users/cgoulding/.m2/repository"
)
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
name := "sbt-plugin"
organization := "com.monadiccloud.bindingz"
version := "1.0-SNAPSHOT"

headerLicense := Some(HeaderLicense.ALv2("2019", "Connor Goulding"))

sbtPlugin := true
crossScalaVersions := Seq("2.10.7", "2.11.11", "2.12.8", "2.13.0-M5")

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" %% "contract-annotations4s" % "1.0-SNAPSHOT",
  "org.scala-sbt" %% "main-settings" % sbtVersion.value,
  "com.dorkbox" % "Annotations" % "2.14",
  "com.github.aishfenton" %% "argus" % "0.2.7",
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "com.fasterxml.jackson.module" % "jackson-module-jsonSchema" % "2.9.8",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"
)
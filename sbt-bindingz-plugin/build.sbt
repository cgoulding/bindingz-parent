name := "sbt-bindingz-plugin"
organization := "com.monadiccloud.bindingz"
version := "1.0-SNAPSHOT"

sbtPlugin := true
crossSbtVersions := Seq("1.2.1")

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" %% "contract-annotations4s" % "1.0-SNAPSHOT",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.dorkbox" % "Annotations" % "2.14",
  "com.github.aishfenton" %% "argus" % "0.2.7",
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "com.fasterxml.jackson.module" % "jackson-module-jsonSchema" % "2.9.8",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"
)
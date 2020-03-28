name := "sbt-bindingz-plugin"
organization := "com.monadiccloud.bindingz"
version := "1.1.3"

sbtPlugin := true
crossSbtVersions := Seq("1.2.1")

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" % "contract-annotations4j" % "1.1.3",
  "com.monadiccloud.bindingz" % "contract-registry-client-java" % "1.1.3",
  "com.dorkbox" % "Annotations" % "2.14",
  "com.fasterxml.jackson.module" % "jackson-module-jsonSchema" % "2.9.8",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"
)

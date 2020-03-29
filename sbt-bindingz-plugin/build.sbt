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
sonatypeProfileName := "com.monadiccloud"

// Open-source license of your choice
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("cgoulding", "sbt-bindingz-plugin", "connor.goulding@gmail.com"))

homepage := Some(url("https://github.com/cgoulding/bindingz-parent"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/cgoulding/bindingz-parent"),
    "scm:git:git://github.com/cgoulding/bindingz-parent.git"
  )
)
developers := List(
  Developer(
    id="cgoulding",
    name="Connor Goulding",
    email="connor.goulding@gmail.com",
    url=url("https://github.com/cgoulding/bindingz-parent")
  )
)

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" % "contract-annotations4j" % "1.1.3",
  "com.monadiccloud.bindingz" % "contract-registry-client-java" % "1.1.3",
  "com.dorkbox" % "Annotations" % "2.14",
  "com.fasterxml.jackson.module" % "jackson-module-jsonSchema" % "2.9.8",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"
)

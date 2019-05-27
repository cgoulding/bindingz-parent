
name := "contract-annotations4s"
organization := "com.monadiccloud.bindingz"
version := "1.0-SNAPSHOT"

crossScalaVersions := Seq("2.11.11", "2.12.6")

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

useGpg := true

organizationName := name.value
organizationHomepage := Some(url("https://github.com/cgoulding/bindingz-parent"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/cgoulding/bindingz-parent"),
    "scm:git@github.com:cgoulding/bindingz-parent.git"
  )
)
developers := List(
  Developer(
    id    = "cgoulding",
    name  = "Connor Goulding",
    email = "connor.goulding@gmail.com",
    url   = url("https://github.com/cgoulding/bindingz-parent")
  )
)

description := "Bindingz for JSON communication"
licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/cgoulding/bindingz-parent"))

// Remove all additional repository other than Maven Central from POM
pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true
publishArtifact in Test := false
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
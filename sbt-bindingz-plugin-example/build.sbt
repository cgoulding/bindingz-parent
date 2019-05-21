name := "sbt-plugin-example"
organization := "com.monadiccloud.bindingz"
version := "1.0-SNAPSHOT"

headerLicense := Some(HeaderLicense.ALv2("2019", "Connor Goulding"))

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" %% "contract-annotations4s" % "1.0-SNAPSHOT"
)

import com.monadiccloud.bindingz.contract.plugin.sbt._
bindingzBroker := "http://localhost:8080"
bindingzProcessConfigurations := Seq(
  BindingzProcessConfiguration("sbt-plugin-example", "SampleDto", "v2", "com.monadiccloud.sample.latest")
)
bindingzPublishConfigurations := Seq(
  BindingzPublishConfiguration("com.monadiccloud")
)
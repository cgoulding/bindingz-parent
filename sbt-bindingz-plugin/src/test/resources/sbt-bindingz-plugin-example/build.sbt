name := "sbt-plugin-example"
organization := "com.monadiccloud.bindingz"
version := "1.0-SNAPSHOT"

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" % "contract-annotations4j" % "1.1-SNAPSHOT"
)

import com.monadiccloud.bindingz.contract.plugin.sbt._
bindingzRegistry := "http://localhost:8080"
bindingzProcessConfigurations := Seq(
  BindingzProcessConfiguration("asdf", "asdf", "asdf", "com.monadiccloud.sample.latest", "FooBar")
)
bindingzPublishConfigurations := Seq(
  BindingzPublishConfiguration("com.monadiccloud")
)

name := "sbt-plugin-example"
organization := "com.monadiccloud.bindingz"
version := "1.1-SNAPSHOT"

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" % "contract-annotations4j" % "1.1-SNAPSHOT"
)

import com.monadiccloud.bindingz.contract.plugin.sbt._
bindingzRegistry := "http://localhost:7070"
bindingzProcessConfigurations := Seq(
  BindingzProcessConfiguration(
    owner = "asdf",
    contractName = "asdf",
    version = "asdf",
    packageName = "com.monadiccloud.sample.latest",
    className = "FooBar",
    factoryConfiguration = Map(
      "targetLanguage" -> "SCALA"
    )
  )
)
bindingzPublishConfigurations := Seq(
  BindingzPublishConfiguration("com.monadiccloud")
)

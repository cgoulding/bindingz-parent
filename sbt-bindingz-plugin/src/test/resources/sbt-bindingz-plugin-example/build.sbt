name := "sbt-plugin-example"
organization := "com.monadiccloud.bindingz"
version := "1.1.3"

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.monadiccloud.bindingz" % "contract-annotations4j" % "1.1.3"
)

import com.monadiccloud.bindingz.contract.plugin.sbt._
bindingzApiKey := "cPrS6nyv.ac5bc2f16417e79f2517531c2f6a0591"
bindingzProcessConfigurations := Seq(
  BindingzProcessConfiguration(
    namespace = "default",
    owner = "sbt-plugin-example",
    contractName = "SampleDto",
    version = "v4",
    packageName = "com.monadiccloud.sample.latest",
    className = "FooBar",
    providerConfiguration = Map(
      "targetLanguage" -> "SCALA"
    )
  )
)
bindingzPublishConfigurations := Seq(
  BindingzPublishConfiguration("com.monadiccloud")
)

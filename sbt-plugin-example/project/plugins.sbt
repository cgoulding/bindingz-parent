resolvers += Classpaths.sbtPluginReleases

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.monadiccloud.bindingz" %% "sbt-plugin" % "1.0-SNAPSHOT")
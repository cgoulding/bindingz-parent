resolvers += Classpaths.sbtPluginReleases

resolvers += Resolver.mavenLocal
resolvers += Resolver.mavenCentral
resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.monadiccloud.bindingz" %% "sbt-bindingz-plugin" % "1.1.3")

addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.2.0")
/*
 * Copyright (c) 2019 Connor Goulding
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monadiccloud.bindingz.contract.plugin.sbt

import java.nio.file.Paths

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.monadiccloud.bindingz.contract.plugin.sbt.resources._
import sbt.Keys.{compile, fullClasspath, resourceDirectories, sourceDirectories}
import sbt.{AutoPlugin, Compile, Def, File, IO, PluginTrigger, Plugins, Setting, file, plugins}

import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader

object BindingzPlugin extends AutoPlugin {

  override val trigger: PluginTrigger = allRequirements
  override val requires: Plugins = plugins.JvmPlugin

  val objectMapper = new ObjectMapper().registerModule(new DefaultScalaModule())

  object autoImport extends BindingzKeys

  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    bindingzRegistry := "http://localhost:8080",

    bindingzTargetSourceDirectory := file("target/generated-sources/bindingz"),
    bindingzTargetResourceDirectory := file("target/generated-resources/bindingz"),
    bindingzDistributionResourceDirectory := file("target/bindingz/distribution"),

    bindingzProcessConfigurations := Seq(),
    bindingzPublishConfigurations := Seq(),

    bindingzProcessResources := Def.sequential(
      processResources,
      compile in Compile
    ).value,

    bindingzPublishResources := publishResources.value
  )

  def processResources =  Def.task {
    val sourceCodeRepository = new SourceCodeRepository(bindingzTargetSourceDirectory.value.toPath)

    bindingzProcessConfigurations.value.map(c => {
      val response = scala.io.Source.fromURL(s"${bindingzRegistry.value}/api/v1/schemas/${c.providerName}/${c.contractName}?version=${c.version}")
      val resource = objectMapper.readValue(response.mkString, classOf[SchemaResource])
      val fileName = Paths.get(bindingzTargetResourceDirectory.value.toString, c.providerName, c.contractName, c.version)

      IO.write(fileName.toFile, objectMapper.writeValueAsString(resource.content.schema))

      val sourceCode = SourceCodeFactory.create(
        objectMapper.writeValueAsString(resource.content.schema),
        c.contractName,
        Some(c.packageName))

      sourceCodeRepository.save(c.contractName, Some(c.packageName), sourceCode)
    })

    resourceDirectories.in(Compile) += bindingzTargetResourceDirectory.value
    sourceDirectories.in(Compile) += bindingzTargetSourceDirectory.value
  }

  def publishResources =  Def.task {
    val cp: Seq[File] = (fullClasspath in Compile).value.files
    val classLoader = new URLClassLoader(cp.map(c => c.toURI.toURL), this.getClass.getClassLoader)
    val resourceRepository = new ResourceRepository(bindingzRegistry.value, bindingzDistributionResourceDirectory.value.toPath)

    bindingzPublishConfigurations.value.map(c => {
      val resources = ResourceFactory.create(classLoader, c.scanBasePackage)
      resources.foreach(resourceRepository.save)
    })
    resourceRepository.export()
  }
}

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
import com.monadiccloud.bindingz.contract.registry.client.ContractRegistryClient
import com.monadiccloud.bindingz.contract.registry.client.configuration.SourceCodeConfiguration
import sbt.Keys.{compile, fullClasspath, resourceDirectories, sourceDirectories}
import sbt.{AutoPlugin, Compile, Def, File, IO, PluginTrigger, Plugins, Setting, file, plugins}

import scala.collection.JavaConverters._
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

    bindingzProcessConfigurations := Seq(),
    bindingzPublishConfigurations := Seq(),

    bindingzProcessResources := Def.sequential(
      processResources,
      compile in Compile
    ).value,

    bindingzPublishResources := publishResources.value
  )

  def processResources =  Def.task {
    val client = new ContractRegistryClient(bindingzRegistry.value)

    bindingzProcessConfigurations.value.map(c => {
      val sourceCodeConfiguration = new SourceCodeConfiguration()
      sourceCodeConfiguration.setPackageName(c.packageName)
      sourceCodeConfiguration.setClassName(c.className)

      val source = client.generateSources(
        c.providerName,
        c.contractName,
        c.version,
        sourceCodeConfiguration
      )

      val resourcePath = Paths.get(bindingzTargetResourceDirectory.value.toString, c.providerName, c.contractName, c.version)
      resourcePath.getParent.toFile.mkdir()

      IO.write(resourcePath.toFile, objectMapper.writeValueAsString(source.getContent().getSchema))

      source.getSources.asScala.foreach(s => {
        val sourcePath = Paths.get(bindingzTargetSourceDirectory.value.toString, s.getFile.asScala.toArray:_*)
        sourcePath.getParent.toFile.mkdir()

        IO.write(sourcePath.toFile, s.getContent)
      })
    })

    resourceDirectories.in(Compile) += bindingzTargetResourceDirectory.value
    sourceDirectories.in(Compile) += bindingzTargetSourceDirectory.value
  }

  def publishResources =  Def.task {
    val cp: Seq[File] = (fullClasspath in Compile).value.files
    val classLoader = new URLClassLoader(cp.map(c => c.toURI.toURL), this.getClass.getClassLoader)
    val client = new ContractRegistryClient(bindingzRegistry.value)

    bindingzPublishConfigurations.value.map(c => {
      val resources = ContractFactory.create(classLoader, c.scanBasePackage)
      resources.foreach(client.publishContract)
    })
  }
}

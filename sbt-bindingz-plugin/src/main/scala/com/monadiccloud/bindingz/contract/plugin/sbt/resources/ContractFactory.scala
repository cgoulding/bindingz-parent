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

package com.monadiccloud.bindingz.contract.plugin.sbt.resources

import java.lang.annotation.ElementType

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.{JsonSchema, JsonSchemaGenerator}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.monadiccloud.bindingz.contract.annotations4j.Contract
import com.monadiccloud.bindingz.contract.registry.client.model.ContractDto
import dorkbox.annotation.{AnnotationDefaults, AnnotationDetector}

import scala.collection.JavaConverters._

object ContractFactory {

  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def create(classLoader: ClassLoader, packageNames: String*) = {
    val contractClasses = AnnotationDetector.
      scanClassPath(classLoader, packageNames.toArray: _*).
      forAnnotations(classOf[Contract]).
      on(ElementType.TYPE).
      collect(AnnotationDefaults.getType)

    contractClasses.asScala.map(createResource).toSeq
  }

  private def createResource(contract: Class[_]) = {
    val schemaGen: JsonSchemaGenerator = new JsonSchemaGenerator(mapper)
    val annotations: Array[Contract] = contract.getAnnotationsByType(classOf[Contract])
    val schema: JsonSchema = schemaGen.generateSchema(contract)
    val annotation = annotations(0)
    val resourceContent = new ContractDto(
      annotation.namespace(),
      annotation.owner(),
      annotation.contractName(),
      annotation.version(),
      schema
    )
    resourceContent
  }
}

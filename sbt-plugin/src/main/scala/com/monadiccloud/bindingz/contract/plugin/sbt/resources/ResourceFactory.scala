package com.monadiccloud.bindingz.contract.plugin.sbt.resources

import java.lang.annotation.ElementType

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.{JsonSchema, JsonSchemaGenerator}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.monadiccloud.bindingz.contract.annotations4s.Contract
import dorkbox.annotation.{AnnotationDefaults, AnnotationDetector}

import scala.collection.JavaConverters._

object ResourceFactory {

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
    val contractProvider = annotations(0)
    val resourceContent = SchemaResourceContent(
      contractProvider.contractName(),
      contractProvider.providerName(),
      contractProvider.version(),
      schema
    )
    resourceContent
  }
}

package com.monadiccloud.bindingz.contract.plugin.gradle.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator
import com.monadiccloud.bindingz.contract.annotations4j.Contract
import dorkbox.annotation.AnnotationDefaults
import dorkbox.annotation.AnnotationDetector

import java.lang.annotation.ElementType

class ResourceFactory {

  ObjectMapper mapper = new ObjectMapper()

  Collection<SchemaResourceContent> create(ClassLoader classLoader, String... packageNames) {
    def contractClasses = AnnotationDetector.
      scanClassPath(classLoader, packageNames).
      forAnnotations(Contract.class).
      on(ElementType.TYPE).
      collect(AnnotationDefaults.getType)

    contractClasses.collect{
      createResource(it)
    }
  }

  private SchemaResourceContent createResource(Class contract) {
    JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper)
    JsonSchema schema = schemaGen.generateSchema(contract)

    Contract[] annotations = contract.getAnnotationsByType(Contract.class)
    Contract contractProvider = annotations[0]

    SchemaResourceContent naavroResource = new SchemaResourceContent()
    naavroResource.contractName = contractProvider.contractName()
    naavroResource.providerName = contractProvider.providerName()
    naavroResource.version = contractProvider.version()
    naavroResource.schema = schema
    naavroResource
  }
}
package com.monadiccloud.bindingz.contract.plugin.sbt.resources

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.jsonSchema.JsonSchema

@JsonIgnoreProperties(ignoreUnknown = true)
final case class SchemaResourceContent(contractName: String,
                                       providerName: String,
                                       version: String,
                                       schema: JsonSchema)

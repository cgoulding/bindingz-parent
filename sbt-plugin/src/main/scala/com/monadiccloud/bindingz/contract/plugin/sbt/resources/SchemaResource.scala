package com.monadiccloud.bindingz.contract.plugin.sbt.resources

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
final case class SchemaResource(content: SchemaResourceContent)

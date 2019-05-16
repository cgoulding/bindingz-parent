package com.monadiccloud.bindingz.contract.plugin.gradle.resources

import com.fasterxml.jackson.module.jsonSchema.JsonSchema

class SchemaResourceContent {
    String contractName
    String providerName
    String version
    JsonSchema schema
}

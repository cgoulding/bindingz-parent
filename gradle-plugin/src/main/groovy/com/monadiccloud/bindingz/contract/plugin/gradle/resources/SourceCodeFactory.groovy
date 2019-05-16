package com.monadiccloud.bindingz.contract.plugin.gradle.resources

import com.monadiccloud.bindingz.contract.plugin.gradle.extension.ProcessConfiguration
import org.jsonschema2pojo.Jsonschema2Pojo

class SourceCodeFactory {

  def create(ProcessConfiguration configuration) {
    switch (configuration.providerType) {
      case ProviderType.JSONSCHEMA2POJO: return Jsonschema2Pojo.generate(configuration.jsonSchema2PojoConfiguration)
      default: throw new UnsupportedOperationException("Unknown provider type: " + configuration.providerType)
    }
  }
}

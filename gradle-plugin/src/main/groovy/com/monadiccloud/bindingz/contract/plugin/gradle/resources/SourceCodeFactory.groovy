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

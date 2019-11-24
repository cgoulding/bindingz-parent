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

package com.monadiccloud.bindingz.contract.plugin.maven.resources;

import java.io.File;
import java.io.IOException;

import org.jsonschema2pojo.Jsonschema2Pojo;
import com.monadiccloud.bindingz.contract.plugin.maven.ProcessConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.providers.JsonSchema2PojoConfiguration;

public class SourceCodeFactory {

  private final File targetSourceDirectory;
  private final File targetResourceDirectory;

  public SourceCodeFactory(File targetSourceDirectory, File targetResourceDirectory) {
    this.targetSourceDirectory = targetSourceDirectory;
    this.targetResourceDirectory = targetResourceDirectory;
  }

  public void create(ProcessConfiguration configuration) throws IOException {
    switch (configuration.getProviderType()) {
      case JSONSCHEMA2POJO: Jsonschema2Pojo.generate(
        new JsonSchema2PojoConfiguration(
                targetSourceDirectory,
                targetResourceDirectory,
                configuration.getProviderName(),
                configuration.getContractName(),
                configuration.getVersion(),
                configuration.getPackageName(),
                configuration.getClassName()
        ));
        break;
      default: throw new UnsupportedOperationException("Unknown provider type: " + configuration.getProviderType());
    }
  }
}

/*
 * Copyright 2019 Connor Goulding
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

package com.monadiccloud.bindingz.contract.plugin.gradle.extension

import com.monadiccloud.bindingz.contract.plugin.gradle.providers.JsonSchema2PojoConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.resources.ProviderType

class ProcessConfiguration {

    // naavro config
    File targetSourceDirectory
    File targetResourceDirectory

    String name
    String providerName
    String contractName
    String version

    String packageName
    String className

    ProviderType providerType;
    JsonSchema2PojoConfiguration jsonSchema2PojoConfiguration

    ProcessConfiguration(String name) {
        this.name = name
    }

    def jsonSchema2Pojo(final Closure configureClosure) {
        providerType = ProviderType.JSONSCHEMA2POJO
        jsonSchema2PojoConfiguration = new JsonSchema2PojoConfiguration(
                targetSourceDirectory,
                targetResourceDirectory,
                providerName,
                contractName,
                version,
                packageName,
                className
        )

        configureClosure.delegate = jsonSchema2PojoConfiguration
        configureClosure.call()
    }
}

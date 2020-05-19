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

package com.monadiccloud.bindingz.contract.plugin.gradle.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.monadiccloud.bindingz.contract.plugin.gradle.extension.PublishConfiguration
import com.monadiccloud.bindingz.contract.registry.client.ContractRegistryClient
import com.monadiccloud.bindingz.contract.registry.client.ContractService
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

class PublishResourcesTask extends DefaultTask {

    @Internal
    String registry

    @Internal
    String apiKey

    @Internal
    NamedDomainObjectContainer<PublishConfiguration> publishConfigurations

    @Internal
    ClassLoader classLoader;

    ObjectMapper mapper = new ObjectMapper()

    PublishResourcesTask() {
        description = 'Publishes contract.'
        group = 'Build'
    }

    @TaskAction
    def generate() {
        def client = new ContractRegistryClient(registry, apiKey, mapper)
        System.out.println(registry + " " + apiKey);

        def service = new ContractService(mapper)

        println("PublishConfigurations: " + publishConfigurations.size())
        publishConfigurations.forEach { p ->
            service.create(classLoader, p.scanBasePackage).forEach { c ->
                client.publishContract(c)
            }
        }
    }
}

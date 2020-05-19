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
import com.monadiccloud.bindingz.contract.core.configuration.SourceCodeConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.extension.ProcessConfiguration
import com.monadiccloud.bindingz.contract.registry.client.ContractRegistryClient
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class ProcessResourcesTask extends DefaultTask {

    @Internal
    String registry;

    @Internal
    String apiKey;

    @OutputDirectory
    File targetSourceDirectory;

    @OutputDirectory
    File targetResourceDirectory;

    @Internal
    NamedDomainObjectContainer<ProcessConfiguration> processConfigurations

    ObjectMapper mapper = new ObjectMapper()

    ProcessResourcesTask() {
        description = 'Generates Java classes from a json schema.'
        group = 'Build'
        setOnlyIf { true }
        outputs.upToDateWhen { false }
    }
    @TaskAction
    def generate() {
        println("ProcessConfigurations: " + processConfigurations.size())

        def client = new ContractRegistryClient(registry, apiKey, mapper)
        processConfigurations.forEach { c ->
            def configuration = new SourceCodeConfiguration()
            configuration.setClassName(c.className)
            configuration.setPackageName(c.packageName)
            configuration.setSourceCodeProvider(c.getSourceCodeProvider())
            configuration.setProviderConfiguration(c.getSourceCodeConfiguration())

            def resource = client.generateSources(c.namespace, c.owner, c.contractName, c.version, configuration)
            if (resource != null) {
                if (resource.getSources() != null) {
                    resource.getSources().forEach{ s ->
                        try {
                            def path = Paths.get(
                                    targetSourceDirectory.getAbsolutePath(),
                                    s.getFile().toArray(new String[s.getFile().size()])
                            )
                            path.toFile().getParentFile().mkdirs()
                            Files.write(path, s.getContent().getBytes(), StandardOpenOption.CREATE)
                        } catch (IOException e) {
                            e.printStackTrace()
                        }
                    };
                }

                if (resource.getContent() != null) {
                    def schema = resource.getContent()
                    try {
                        def path = Paths.get(
                                targetResourceDirectory.getAbsolutePath(),
                                schema.getNamespace(),
                                schema.getOwner(),
                                schema.getContractName(),
                                schema.getVersion()
                        )
                        path.toFile().getParentFile().mkdirs()
                        Files.write(path, mapper.writeValueAsBytes(schema.getSchema()), StandardOpenOption.CREATE)
                    } catch (IOException e) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

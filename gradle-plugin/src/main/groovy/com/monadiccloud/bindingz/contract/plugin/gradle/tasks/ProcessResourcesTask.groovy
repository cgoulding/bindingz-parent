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

package com.monadiccloud.bindingz.contract.plugin.gradle.tasks

import com.monadiccloud.bindingz.contract.plugin.gradle.extension.ProcessConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.resources.SourceCodeFactory
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

class ProcessResourcesTask extends DefaultTask {

    String broker;

    File targetSourceDirectory;

    File targetResourceDirectory;

    NamedDomainObjectContainer<ProcessConfiguration> consumerConfigurations

    ProcessResourcesTask() {
        description = 'Generates Java classes from a json schema.'
        group = 'Build'
        outputs.upToDateWhen { false }
    }

    @TaskAction
    def generate() {
        consumerConfigurations.forEach { c ->
            c.jsonSchema2PojoConfiguration.targetResourceDirectory = targetResourceDirectory
            c.jsonSchema2PojoConfiguration.targetSourceDirectory = targetSourceDirectory

            def response = httpGet(broker, c.providerName, c.contractName, c.version)
            def resource = new JsonSlurper().parseText(response)

            if (resource != null && resource.content != null) {
                def file = Paths.get(targetResourceDirectory.toString(), c.providerName, c.contractName, c.version, c.className).toFile()

                file.getParentFile().mkdirs()
                file.write(JsonOutput.toJson(resource.content.schema))
                new SourceCodeFactory().create(c)
            } else {
                println(String.format("Unable to find resource content for %s, %s, %s",
                        c.providerName,
                        c.contractName,
                        c.version))
            }
        }
    }

    def httpGet(String brokerString, String providerName, String contractName, String version) {
        String url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
                brokerString,
                providerName,
                contractName,
                version)

        def p = ['curl', url].execute()
        p.text
    }
}

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

import com.monadiccloud.bindingz.contract.plugin.gradle.extension.PublishConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.resources.ResourceFactory
import com.monadiccloud.bindingz.contract.plugin.gradle.resources.ResourceRepository
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction

class PublishResourcesTask extends DefaultTask {

    String broker

    File targetDistributionDirectory

    NamedDomainObjectContainer<PublishConfiguration> producerConfigurations

    ClassLoader classLoader;

    PublishResourcesTask() {
        description = 'Publishes contract.'
        group = 'Build'
    }

    @TaskAction
    def generate() {
        def repository = new ResourceRepository(broker, targetDistributionDirectory.toPath())
        def factory = new ResourceFactory()
        producerConfigurations.forEach { p ->
            factory.create(classLoader, p.scanBasePackage).forEach { c ->
                repository.save(c)
            }
        }
        repository.export()
    }
}

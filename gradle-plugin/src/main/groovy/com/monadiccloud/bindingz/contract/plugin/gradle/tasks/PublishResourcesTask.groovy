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

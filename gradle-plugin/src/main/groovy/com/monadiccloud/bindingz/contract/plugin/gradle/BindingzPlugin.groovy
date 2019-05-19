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

package com.monadiccloud.bindingz.contract.plugin.gradle

import com.monadiccloud.bindingz.contract.plugin.gradle.extension.ProcessConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.extension.BindingzExtension
import com.monadiccloud.bindingz.contract.plugin.gradle.extension.PublishConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.tasks.ProcessResourcesTask
import com.monadiccloud.bindingz.contract.plugin.gradle.tasks.PublishResourcesTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class BindingzPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = 'bindingz'

    @Override
    void apply(Project project) {
        setupExtension(project)
        configureDirectories(project)
        createTasks(project)
    }

    private def setupExtension(final Project project) {
        def configuration = project.extensions.create(EXTENSION_NAME, BindingzExtension)

        configuration.producerConfigurations = project.container(PublishConfiguration)
        configuration.consumerConfigurations = project.container(ProcessConfiguration)

        configuration
    }

    def configureDirectories(final Project project) {
        project.afterEvaluate {
            def configuration = project.extensions.getByType(BindingzExtension)

            configuration.targetSourceDirectory = configuration.targetSourceDirectory ?: project.file("target/generated-sources/bindingz")
            configuration.targetResourceDirectory = configuration.targetResourceDirectory ?: project.file("target/generated-resources/bindingz")
            configuration.targetDistributionDirectory = configuration.targetDistributionDirectory ?: project.file("target/bindingz/distribution")

            configuration.targetSourceDirectory.mkdirs()
            configuration.targetResourceDirectory.mkdirs()
            configuration.targetDistributionDirectory.mkdirs()
        }
    }

    def createTasks(final Project project) {
        project.afterEvaluate {
            def configuration = project.extensions.getByType(BindingzExtension)

            createProcessResourcesTask(project, configuration)
            createPublishResourcesTask(project, configuration)
        }
    }

    private void createProcessResourcesTask(Project project, BindingzExtension configuration) {
        def processResourcesTask = project.tasks.create "bindingzProcessResources", ProcessResourcesTask

        processResourcesTask.consumerConfigurations = configuration.consumerConfigurations
        processResourcesTask.broker = configuration.broker

        processResourcesTask.targetSourceDirectory = configuration.targetSourceDirectory
        processResourcesTask.targetResourceDirectory = configuration.targetResourceDirectory

        if (project.plugins.hasPlugin('java')) {
            project.sourceSets.main.java.srcDirs += [configuration.targetSourceDirectory]
            project.sourceSets.main.resources.srcDirs += [configuration.targetResourceDirectory]
            project.tasks.compileJava.dependsOn(processResourcesTask)
        } else {
            throw new GradleException('n: Java plugin is required')
        }
    }

    private void createPublishResourcesTask(Project project, BindingzExtension configuration) {
        def publishResourcesTask = project.tasks.create "bindingzPublishResources", PublishResourcesTask

        publishResourcesTask.producerConfigurations = configuration.producerConfigurations
        publishResourcesTask.broker = configuration.broker
        publishResourcesTask.targetDistributionDirectory = configuration.targetDistributionDirectory

        if (project.plugins.hasPlugin('java')) {
            Set<File> classFiles = project.sourceSets.main.runtimeClasspath.getFiles()
            URL[] urls = classFiles.collect { it.toURI().toURL() }.toArray() as URL[]
            publishResourcesTask.classLoader = new URLClassLoader(urls, this.getClass().getClassLoader())
            publishResourcesTask.dependsOn(project.tasks.compileJava)
        } else {
            throw new GradleException('n: Java plugin is required')
        }
    }
}
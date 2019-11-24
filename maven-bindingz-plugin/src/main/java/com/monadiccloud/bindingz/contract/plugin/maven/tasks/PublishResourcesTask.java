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

package com.monadiccloud.bindingz.contract.plugin.maven.tasks;

import com.monadiccloud.bindingz.contract.plugin.maven.PublishConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.ResourceFactory;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.ResourceRepository;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SchemaResourceContent;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PublishResourcesTask implements ExecutableTask {

    private final String registry;
    private final File targetDistributionDirectory;
    private final Collection<PublishConfiguration> producerConfigurations;
    private final ClassLoader classLoader;

    public PublishResourcesTask(String registry, File targetDistributionDirectory, Collection<PublishConfiguration> producerConfigurations, ClassLoader classLoader) {
        this.registry = registry;
        this.targetDistributionDirectory = targetDistributionDirectory;
        this.producerConfigurations = producerConfigurations;
        this.classLoader = classLoader;
    }

    public void execute() throws IOException {
        ResourceRepository repository = new ResourceRepository(registry, targetDistributionDirectory.toPath());
        ResourceFactory factory = new ResourceFactory();

        for (PublishConfiguration p : producerConfigurations) {
            for (SchemaResourceContent c : factory.create(classLoader, p.getScanBasePackage())) {
                repository.save(c);
            }
        }
        repository.export();
    }
}
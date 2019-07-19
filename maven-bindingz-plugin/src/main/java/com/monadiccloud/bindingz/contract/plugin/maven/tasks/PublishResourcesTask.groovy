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

import com.monadiccloud.bindingz.contract.plugin.maven.extension.PublishConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.ResourceFactory;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.ResourceRepository;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SchemaResourceContent;

class PublishResourcesTask {

    private String registry;
    private File targetDistributionDirectory;
    private Collection<PublishConfiguration> producerConfigurations;
    private ClassLoader classLoader;

    void generate() {
        ResourceRepository repository = new ResourceRepository(registry, targetDistributionDirectory.toPath());
        ResourceFactory factory = new ResourceFactory();

        System.out.println("ProducerConfigurations: " + producerConfigurations.size());
        for (PublishConfiguration p : producerConfigurations) {
            for (SchemaResourceContent c : factory.create(classLoader, p.scanBasePackage)) {
                repository.save(c)
            }
        }
        repository.export();
    }
}

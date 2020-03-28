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
import com.monadiccloud.bindingz.contract.registry.client.ContractRegistryClient;
import com.monadiccloud.bindingz.contract.registry.client.model.ContractDto;

import java.io.IOException;
import java.util.Collection;

public class PublishResourcesTask implements ExecutableTask {

    private final String registry;
    private final String apiKey;
    private final Collection<PublishConfiguration> publishConfigurations;
    private final ClassLoader classLoader;

    public PublishResourcesTask(String registry, String apiKey, Collection<PublishConfiguration> publishConfigurations, ClassLoader classLoader) {
        this.registry = registry;
        this.apiKey = apiKey;
        this.publishConfigurations = publishConfigurations;
        this.classLoader = classLoader;
    }

    public void execute() throws IOException {
        ContractRegistryClient client = new ContractRegistryClient(registry, apiKey);
        ContractFactory factory = new ContractFactory();

        for (PublishConfiguration p : publishConfigurations) {
            for (ContractDto c : factory.create(classLoader, p.getScanBasePackage())) {
                client.publishContract(c);
            }
        }
    }
}

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.plugin.maven.extension.ProcessConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SchemaResource;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SourceCodeFactory;

public class ProcessResourcesTask {

    private ObjectMapper mapper = new ObjectMapper();

    private String registry;
    private File targetSourceDirectory;
    private File targetResourceDirectory;
    private Collection<ProcessConfiguration> consumerConfigurations;

    public void generate() throws IOException {
        for (ProcessConfiguration c : consumerConfigurations) {
            c.getJsonSchema2PojoConfiguration().setTargetResourceDirectory(targetResourceDirectory);
            c.getJsonSchema2PojoConfiguration().setTargetSourceDirectory(targetSourceDirectory);

            String response = httpGet(registry, c.getProviderName(), c.getContractName(), c.getVersion());
            SchemaResource resource = mapper.readValue(response, SchemaResource.class);

            if (resource != null && resource.getContent() != null) {
                File file = Paths.get(targetResourceDirectory.toString(), c.getProviderName(), c.getContractName(), c.getVersion(), c.getClassName()).toFile();

                file.getParentFile().mkdirs();
                mapper.writeValue(file, resource.getContent().getSchema());
                new SourceCodeFactory().create(c);
            } else {
                System.out.println(String.format("Unable to find resource content for %s, %s, %s",
                        c.getProviderName(),
                        c.getContractName(),
                        c.getVersion()));
            }
        };
    }

    private String httpGet(String registryString, String providerName, String contractName, String version) {
        String url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
                registryString,
                providerName,
                contractName,
                version);
        return "TODO";
    }
}

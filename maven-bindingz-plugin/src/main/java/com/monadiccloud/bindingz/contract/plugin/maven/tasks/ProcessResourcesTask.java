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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.plugin.maven.ProcessConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SchemaDto;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SourceCodeConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.SourceResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public class ProcessResourcesTask implements ExecutableTask {

    private final ObjectMapper mapper = new ObjectMapper();

    private final String registry;
    private final File targetSourceDirectory;
    private final File targetResourceDirectory;
    private final Collection<ProcessConfiguration> processConfigurations;

    public ProcessResourcesTask(String registry, File targetSourceDirectory, File targetResourceDirectory, Collection<ProcessConfiguration> processConfigurations) {
        this.registry = registry;
        this.targetSourceDirectory = targetSourceDirectory;
        this.targetResourceDirectory = targetResourceDirectory;
        this.processConfigurations = processConfigurations;
    }

    public void execute() throws IOException {
        for (ProcessConfiguration c : processConfigurations) {
            SourceCodeConfiguration configuration = new SourceCodeConfiguration();
            configuration.setClassName(c.getClassName());
            configuration.setPackageName(c.getPackageName());
            configuration.setProviderType(c.getCodeProviderType());
            configuration.setProviderConfiguration(c.getCodeProviderConfiguration());

            SourceResource resource = requestSource(registry, c.getProviderName(), c.getContractName(), c.getVersion(), configuration);
            if (resource != null) {
                if (resource.getSources() != null) {
                    resource.getSources().stream().forEach(s -> {
                        try {
                            Path path = Paths.get(
                                    targetSourceDirectory.getAbsolutePath(),
                                    s.getFile().toArray(new String[s.getFile().size()])
                            );
                            path.toFile().getParentFile().mkdirs();
                            Files.write(path, s.getContent().getBytes(), StandardOpenOption.CREATE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                if (resource.getContent() != null) {
                    SchemaDto schema = resource.getContent();
                    try {
                        Path path = Paths.get(
                                targetResourceDirectory.getAbsolutePath(),
                                schema.getProviderName(),
                                schema.getContractName(),
                                schema.getVersion()
                        );
                        path.toFile().getParentFile().mkdirs();
                        Files.write(path, mapper.writeValueAsBytes(schema.getSchema()), StandardOpenOption.CREATE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private SourceResource requestSource(String registryString,
                                         String providerName,
                                         String contractName,
                                         String version,
                                         SourceCodeConfiguration configuration) {
        String url = String.format("%s/api/v1/sources/%s/%s?version=%s",
                registryString,
                providerName,
                contractName,
                version);

        // POST
        try {
            HttpURLConnection post = (HttpURLConnection)new URL(url).openConnection();
            post.setDoOutput(true);
            post.setRequestProperty("Content-Type", "application/json");
            post.setRequestMethod("POST");
            post.connect();

            String body = mapper.writeValueAsString(configuration);
            post.getOutputStream().write(body.getBytes("UTF-8"));

            int responseCode = post.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(post.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                return mapper.readValue(response.toString(), SourceResource.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

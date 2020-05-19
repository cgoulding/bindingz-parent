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

package com.monadiccloud.bindingz.contract.registry.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.registry.code.providers.jsonschematopojo.JsonSchema2PojoConfiguration;
import com.monadiccloud.bindingz.contract.registry.model.SchemaDto;
import com.monadiccloud.bindingz.contract.registry.model.SourceDto;
import org.jsonschema2pojo.Jsonschema2Pojo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SourceCodeFactory {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<SourceDto> create(SchemaDto schemaDto, SourceCodeConfiguration configuration) throws IOException {
        File targetDirectory = createTargetDirectory();
        switch (configuration.getFactoryType()) {
            case JSONSCHEMA2POJO: {
                String config = mapper.writeValueAsString(configuration.getProviderConfiguration());
                JsonSchema2PojoConfiguration factoryConfig = mapper.readValue(config, JsonSchema2PojoConfiguration.class);
                factoryConfig.setTargetSourceDirectory(targetDirectory);
                factoryConfig.setTargetResourceDirectory(createResourceDirectory(schemaDto, configuration));
                factoryConfig.setPackageName(configuration.getPackageName());
                factoryConfig.setClassName(configuration.getClassName());
                Jsonschema2Pojo.generate(factoryConfig);
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown provider type: " + configuration.getFactoryType());
        }

        return Files.walk(targetDirectory.toPath()).
                filter(f -> Files.isRegularFile(f)).
                map(f -> new SourceDto(
                        file(targetDirectory.toPath(), f),
                        contents(f)
                )).collect(Collectors.toList());
    }

    private String contents(Path file) {
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            return "";
        }
    }

    private List<String> file(Path root, Path source) {
        List<String> parts = new ArrayList<>();
        root.relativize(source).iterator().forEachRemaining(p -> parts.add(p.getFileName().toString()));
        return parts;
    }

    private File createResourceDirectory(SchemaDto schemaDto, SourceCodeConfiguration configuration) throws IOException {
        File tempRoot = File.createTempFile("resource-", ".schema");
        tempRoot.delete();
        tempRoot.mkdir();

        File targetResourceDirectory = Paths.get(tempRoot.toString(), configuration.getClassName() + ".jsd").toFile();
        mapper.writeValue(targetResourceDirectory, schemaDto.getSchema());

        return tempRoot;
    }

    private File createTargetDirectory() throws IOException {
        File targetSourceDirectory = File.createTempFile("target-", ".source");
        targetSourceDirectory.delete();
        targetSourceDirectory.mkdir();
        return targetSourceDirectory;
    }
}

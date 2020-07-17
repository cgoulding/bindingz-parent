/*
 * Copyright (c) 2020 Connor Goulding
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
package io.bindingz.server.repository.filebacked;

import io.bindingz.server.RegistryException;
import io.bindingz.server.model.HashedApiKeyDto;
import io.bindingz.server.repository.ApiKeyRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Profile("filebacked")
public class FileBackedApiKeyRepository implements ApiKeyRepository {

    private static final String REPO = "keys";

    private final String directory;
    private final ObjectMapper mapper = new ObjectMapper();

    public FileBackedApiKeyRepository(
            @Autowired @Value("${repository.filebacked.directory:${user.home}/.bindingz/repository}") String directory) {
        this.directory = directory;
    }

    @Override
    public Optional<String> findClientIdentifier(String apiKey) throws RegistryException {
        File file = Paths.get(directory, REPO, apiKey).toFile();
        if (file.exists()) {
            try {
                HashedApiKeyDto key = mapper.readValue(file, HashedApiKeyDto.class);
                return Optional.of(key.getClientIdentifier());
            } catch (IOException e) {
                throw new RegistryException("Unable to read schema", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveApiKey(HashedApiKeyDto apiKey) throws RegistryException {
        File file = Paths.get(directory, REPO, apiKey.getHash()).toFile();
        try {
            mapper.writeValue(file, apiKey);
        } catch (IOException e) {
            throw new RegistryException("Unable to save key", e);
        }
    }

    @Override
    public List<HashedApiKeyDto> getApiKeys(String clientIdentifier) throws RegistryException {
        try {
            return Files.walk(Paths.get(directory, REPO)).flatMap(file -> {
                try {
                    HashedApiKeyDto key = mapper.readValue(file.toFile(), HashedApiKeyDto.class);
                    return Stream.of(key);
                } catch (IOException e) {
                    return Stream.empty();
                }
            }).filter(key -> clientIdentifier.equals(key.getClientIdentifier())).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RegistryException("Unable to find keys", e);
        }
    }
}

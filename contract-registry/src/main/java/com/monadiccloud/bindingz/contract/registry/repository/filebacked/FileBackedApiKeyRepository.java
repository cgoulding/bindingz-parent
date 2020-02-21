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
package com.monadiccloud.bindingz.contract.registry.repository.filebacked;

import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Profile("filebacked")
public class FileBackedApiKeyRepository implements ApiKeyRepository {

    private static final String REPO = "keys";

    private final String directory;

    public FileBackedApiKeyRepository(
            @Autowired @Value("${repository.filebacked.directory:${user.home}/.bindingz/repository}") String directory) {
        this.directory = directory;
    }

    @Override
    public String findAccountIdentifier(String apiKey) throws RegistryException {
        File file = Paths.get(directory, REPO, apiKey).toFile();
        if (file.exists()) {
            try {
                return Files.readString(file.toPath());
            } catch (IOException e) {
                throw new RegistryException("Unable to read schema", e);
            }
        }
        return null;
    }
}

/*
 * Copyright (c) 2019. Connor Goulding
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.repository.SchemaRepository;
import com.monadiccloud.bindingz.contract.registry.model.SchemaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Profile("filebacked")
public class FileBackedSchemaRepository implements SchemaRepository {

    private static final String REPO = "schemas";

    private final String directory;
    private final ObjectMapper mapper = new ObjectMapper();

    public FileBackedSchemaRepository(
            @Autowired @Value("${repository.filebacked.directory:${user.home}/.bindingz/repository}") String directory) {
        this.directory = directory;
    }

    @Override
    public void add(SchemaDto schemaDto) {
        File file = Paths.get(directory, REPO, getFileName(schemaDto)).toFile();
        file.getParentFile().mkdirs();

        try(FileOutputStream fos = new FileOutputStream(file.toString())) {
            fos.write(mapper.writeValueAsString(schemaDto).getBytes());
        } catch (IOException e) {
            throw new RegistryException("Unable to add schema", e);
        }
    }

    @Override
    public SchemaDto find(String accountIdentifier,
                          String namespace,
                          String providerName,
                          String contractName,
                          String version) {
        File file = Paths.get(directory, REPO, getFileName(new SchemaKey(accountIdentifier, namespace, providerName, contractName, version))).toFile();
        if (file.exists()) {
            try {
                return mapper.readValue(file, SchemaDto.class);
            } catch (IOException e) {
                throw new RegistryException("Unable to read schema", e);
            }
        }
        return null;
    }

    @Override
    public Collection<SchemaDto> findAllByAccount(String accountIdentifier) throws RegistryException {
        try {
            return Files.walk(Paths.get(directory, REPO)).map(file -> {
                try {
                    return Optional.of(mapper.readValue(file.toFile(), SchemaDto.class));
                } catch (IOException e) {
                    return Optional.<SchemaDto>empty();
                }
            }).flatMap(s -> s.isPresent() ? Stream.of(s.get()) : Stream.empty()).
                    filter(schemaDto -> schemaDto.getAccountIdentifier().equals(accountIdentifier)).
                    collect(Collectors.toList());
        } catch (IOException e) {
            throw new RegistryException("Unable to read file", e);
        }
    }

    private String getFileName(SchemaDto schemaDto) {
        return getFileName(new SchemaKey(
                schemaDto.getAccountIdentifier(),
                schemaDto.getNamespace(),
                schemaDto.getProviderName(),
                schemaDto.getContractName(),
                schemaDto.getVersion()));
    }

    private String getFileName(SchemaKey schemaKey) {
        return String.valueOf(schemaKey.hashCode());
    }

    private static class SchemaKey {
        private final String accountIdentifier;
        private final String namespace;
        private final String providerName;
        private final String contractName;
        private final String version;

        public SchemaKey(String accountIdentifier,
                         String namespace,
                         String providerName,
                         String contractName,
                         String version) {
            this.accountIdentifier = accountIdentifier;
            this.namespace = namespace;
            this.contractName = contractName;
            this.providerName = providerName;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SchemaKey schemaKey = (SchemaKey) o;
            return Objects.equals(accountIdentifier, schemaKey.accountIdentifier) &&
                    Objects.equals(namespace, schemaKey.namespace) &&
                    Objects.equals(contractName, schemaKey.contractName) &&
                    Objects.equals(providerName, schemaKey.providerName) &&
                    Objects.equals(version, schemaKey.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountIdentifier, namespace, contractName, providerName, version);
        }
    }
}

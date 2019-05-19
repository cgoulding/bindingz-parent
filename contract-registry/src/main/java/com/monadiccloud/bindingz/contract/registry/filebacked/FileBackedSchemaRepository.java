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

package com.monadiccloud.bindingz.contract.registry.filebacked;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.SchemaDto;
import com.monadiccloud.bindingz.contract.registry.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Profile("filebacked")
public class FileBackedSchemaRepository implements SchemaRepository {

    private final String directory;
    private final ObjectMapper mapper = new ObjectMapper();

    public FileBackedSchemaRepository(
            @Autowired @Value("${repository.filebacked.directory:${user.home}/.bindingz/repository}") String directory) {
       this.directory = directory;
    }

    @Override
    public void add(SchemaDto schemaDto) {
        File file = Paths.get(directory, getFileName(schemaDto)).toFile();
        file.getParentFile().mkdirs();

        try(FileOutputStream fos = new FileOutputStream(file.toString())) {
            fos.write(mapper.writeValueAsString(schemaDto).getBytes());
        } catch (IOException e) {
            throw new RegistryException("Unable to add schema", e);
        }
    }

    @Override
    public SchemaDto find(String contractName,
                          String providerName,
                          String version) {
        File file = Paths.get(directory, getFileName(new SchemaKey(contractName, providerName, version))).toFile();
        if (file.exists()) {
            try {
                return mapper.readValue(file, SchemaDto.class);
            } catch (IOException e) {
                throw new RegistryException("Unable to read schema", e);
            }
        }
        return null;
    }

    private String getFileName(SchemaDto schemaDto) {
        return getFileName(new SchemaKey(schemaDto.getContractName(), schemaDto.getProviderName(), schemaDto.getVersion()));
    }

    private String getFileName(SchemaKey schemaKey) {
        return String.valueOf(schemaKey.hashCode());
    }

    private static class SchemaKey {
        private final String contractName;
        private final String providerName;
        private final String version;

        public SchemaKey(String contractName,
                         String providerName,
                         String version) {
            this.contractName = contractName;
            this.providerName = providerName;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SchemaKey schemaKey = (SchemaKey) o;
            return Objects.equals(contractName, schemaKey.contractName) &&
                    Objects.equals(providerName, schemaKey.providerName) &&
                    Objects.equals(version, schemaKey.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(contractName, providerName, version);
        }
    }
}

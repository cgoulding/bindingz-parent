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

package com.monadiccloud.bindingz.contract.registry.repository.inmemory;

import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.model.SchemaDto;
import com.monadiccloud.bindingz.contract.registry.repository.SchemaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Profile({"inmemory"})
public class InMemorySchemaRepository implements SchemaRepository {

    private Map<SchemaKey, SchemaDto> schemas = new HashMap<>();

    @Override
    public void add(SchemaDto schemaDto) {
        schemas.put(
                new SchemaKey(schemaDto.getAccountIdentifier(),
                        schemaDto.getNamespace(),
                        schemaDto.getContractName(),
                        schemaDto.getProviderName(),
                        schemaDto.getVersion()),
                schemaDto
        );
    }

    @Override
    public SchemaDto find(String accountIdentifier,
                          String namespace,
                          String providerName,
                          String contractName,
                          String version) {
        return schemas.get(new SchemaKey(accountIdentifier, namespace, providerName, contractName, version));
    }

    @Override
    public Collection<SchemaDto> findAllByAccount(String accountIdentifier) throws RegistryException {
        return schemas.values().stream().
                filter(schemaDto -> schemaDto.getAccountIdentifier().equals(accountIdentifier)).
                collect(Collectors.toList());
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
            this.providerName = providerName;
            this.contractName = contractName;
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

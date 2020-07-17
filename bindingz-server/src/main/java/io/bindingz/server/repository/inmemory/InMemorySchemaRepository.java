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

package io.bindingz.server.repository.inmemory;

import io.bindingz.server.RegistryException;
import io.bindingz.server.model.SchemaDto;
import io.bindingz.server.repository.SchemaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Profile({"inmemory"})
public class InMemorySchemaRepository implements SchemaRepository {

    private Map<SchemaKey, SchemaDto> schemas = new HashMap<>();

    @Override
    public void add(SchemaDto schemaDto) {
        schemas.put(
                new SchemaKey(schemaDto.getClientIdentifier(),
                        schemaDto.getNamespace(),
                        schemaDto.getContractName(),
                        schemaDto.getOwner(),
                        schemaDto.getVersion()),
                schemaDto
        );
    }

    @Override
    public Optional<SchemaDto> find(String clientIdentifier,
                                    String namespace,
                                    String owner,
                                    String contractName,
                                    String version) {
        return Optional.ofNullable(schemas.get(new SchemaKey(
                clientIdentifier,
                namespace,
                owner,
                contractName,
                version)));
    }

    @Override
    public Collection<SchemaDto> findAllByClient(String clientIdentifier) throws RegistryException {
        return schemas.values().stream().
                filter(schemaDto -> schemaDto.getClientIdentifier().equals(clientIdentifier)).
                collect(Collectors.toList());
    }

    private static class SchemaKey {
        private final String clientIdentifier;
        private final String namespace;
        private final String owner;
        private final String contractName;
        private final String version;

        public SchemaKey(String clientIdentifier,
                         String namespace,
                         String owner,
                         String contractName,
                         String version) {
            this.clientIdentifier = clientIdentifier;
            this.namespace = namespace;
            this.owner = owner;
            this.contractName = contractName;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SchemaKey schemaKey = (SchemaKey) o;
            return Objects.equals(clientIdentifier, schemaKey.clientIdentifier) &&
                    Objects.equals(namespace, schemaKey.namespace) &&
                    Objects.equals(contractName, schemaKey.contractName) &&
                    Objects.equals(owner, schemaKey.owner) &&
                    Objects.equals(version, schemaKey.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clientIdentifier, namespace, contractName, owner, version);
        }
    }
}

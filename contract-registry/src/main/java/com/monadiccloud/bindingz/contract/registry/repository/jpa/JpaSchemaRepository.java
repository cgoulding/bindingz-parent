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
package com.monadiccloud.bindingz.contract.registry.repository.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.dao.JpaContractDao;
import com.monadiccloud.bindingz.contract.registry.entity.ContractEntity;
import com.monadiccloud.bindingz.contract.registry.repository.SchemaRepository;
import com.monadiccloud.bindingz.contract.registry.model.SchemaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Profile({"prod"})
public class JpaSchemaRepository implements SchemaRepository {

    private final JpaContractDao contractDao;
    private final ObjectMapper mapper = new ObjectMapper();

    public JpaSchemaRepository(@Autowired JpaContractDao contractDao) {
        this.contractDao = contractDao;
    }

    @Override
    public void add(SchemaDto schemaDto) throws RegistryException {
        try {
            ContractEntity contractEntity = new ContractEntity(
                    new ContractEntity.ContractId(
                            schemaDto.getClientIdentifier(),
                            schemaDto.getNamespace(),
                            schemaDto.getOwner(),
                            schemaDto.getContractName(),
                            schemaDto.getVersion()
                    ),
                    mapper.writeValueAsString(schemaDto.getSchema())
            );
            contractDao.save(contractEntity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RegistryException("Unable to save contract", e);
        }
    }

    @Override
    public Optional<SchemaDto> find(String clientIdentifier,
                                    String namespace,
                                    String owner,
                                    String contractName,
                                    String version) throws RegistryException {
        return contractDao.findById(new ContractEntity.ContractId(
                clientIdentifier,
                namespace,
                owner,
                contractName,
                version
        )).map(this::transform);
    }

    @Override
    public Collection<SchemaDto> findAllByClient(String clientIdentifier) throws RegistryException {
        return StreamSupport.stream(contractDao.findByContractIdClientIdentifier(clientIdentifier).spliterator(), false).
                map(this::transform).
                collect(Collectors.toList());
    }

    private SchemaDto transform(ContractEntity contractEntity) {
        try {
            return new SchemaDto(
                    contractEntity.getContractId().getClientIdentifier(),
                    contractEntity.getContractId().getNamespace(),
                    contractEntity.getContractId().getOwner(),
                    contractEntity.getContractId().getName(),
                    contractEntity.getContractId().getRevision(),
                    mapper.readValue(contractEntity.getSchema(), JsonSchema.class)
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable parse schema, should not happen", e);
        }
    }
}

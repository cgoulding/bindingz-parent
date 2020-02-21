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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Profile({"lambda", "prod"})
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
                            schemaDto.getAccountIdentifier(),
                            schemaDto.getNamespace(),
                            schemaDto.getProviderName(),
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
    public SchemaDto find(String accountIdentifier,
                          String namespace,
                          String providerName,
                          String contractName,
                          String version) throws RegistryException {
        return transform(contractDao.findOne(new ContractEntity.ContractId(
                accountIdentifier,
                namespace,
                providerName,
                contractName,
                version
        )));
    }

    @Override
    public Collection<SchemaDto> findAllByAccount(String accountIdentifier) throws RegistryException {
        return StreamSupport.stream(contractDao.findByContractIdAccountIdentifier(accountIdentifier).spliterator(), false).
                map(this::transform).
                collect(Collectors.toList());
    }

    private SchemaDto transform(ContractEntity contractEntity) {
        try {
            return new SchemaDto(
                    contractEntity.getContractId().getAccountIdentifier(),
                    contractEntity.getContractId().getNamespace(),
                    contractEntity.getContractId().getProvider(),
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

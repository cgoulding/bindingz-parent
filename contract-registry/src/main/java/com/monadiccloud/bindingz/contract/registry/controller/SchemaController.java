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

package com.monadiccloud.bindingz.contract.registry.controller;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.monadiccloud.bindingz.contract.registry.code.SourceCodeConfiguration;
import com.monadiccloud.bindingz.contract.registry.code.SourceCodeFactory;
import com.monadiccloud.bindingz.contract.registry.model.SourceDto;
import com.monadiccloud.bindingz.contract.registry.repository.ApiKeyRepository;
import com.monadiccloud.bindingz.contract.registry.repository.SchemaRepository;
import com.monadiccloud.bindingz.contract.registry.model.SchemaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class SchemaController {

    private final SchemaRepository repository;
    private final ApiKeyRepository apiKeyRepository;

    public SchemaController(@Autowired SchemaRepository repository,
                            @Autowired ApiKeyRepository apiKeyRepository) {
        this.repository = repository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/schemas", method = {RequestMethod.GET})
    public ResponseEntity<Collection<SchemaResource>> getAll(@RequestHeader("Authorization") String authorization) {
        System.out.println("Getting ...");

        return new ResponseEntity(repository.findAllByAccount(accountIdentifier(authorization)), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/schemas/{namespace}/{providerName}/{contractName}", method = RequestMethod.GET)
    public ResponseEntity<SchemaResource> getSchema(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("namespace") String namespace,
            @PathVariable("providerName") String providerName,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version) {
        System.out.println("Getting ...");

        SchemaDto schemaDto = repository.find(accountIdentifier(authorization), namespace, providerName, contractName, version.orElse("latest"));
        return new ResponseEntity<>(new SchemaResource(schemaDto), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/schemas/{namespace}/{providerName}/{contractName}", method = RequestMethod.POST)
    public ResponseEntity<SchemaResource> postSchema(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("namespace") String namespace,
            @PathVariable("providerName") String providerName,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version,
            @RequestBody JsonSchema schema) {
        System.out.println("Posting ...");

        String accountIdentifier = accountIdentifier(authorization);
        SchemaDto versioned = new SchemaDto(accountIdentifier, namespace, providerName, contractName, version.orElse("latest"), schema);
        SchemaDto latest = new SchemaDto(accountIdentifier, namespace, providerName, contractName, "latest", schema);

        repository.add(versioned);
        repository.add(latest);

        return new ResponseEntity<>(new SchemaResource(versioned), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/v1/sources/{namespace}/{providerName}/{contractName}", method = RequestMethod.POST)
    public ResponseEntity<SourceResource> createSources(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("namespace") String namespace,
            @PathVariable("providerName") String providerName,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version,
            @RequestBody SourceCodeConfiguration configuration) throws IOException {
        System.out.println("Creating ...");

        SchemaDto schemaDto = repository.find(accountIdentifier(authorization), namespace, providerName, contractName, version.orElse("latest"));
        List<SourceDto> sources = new SourceCodeFactory().create(schemaDto, configuration);

        return new ResponseEntity<>(new SourceResource(schemaDto, sources), HttpStatus.OK);
    }

    private String accountIdentifier(String apiKey) {
        return apiKeyRepository.findAccountIdentifier(apiKey);
    }
}
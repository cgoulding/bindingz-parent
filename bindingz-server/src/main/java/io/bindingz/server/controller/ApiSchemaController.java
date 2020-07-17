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

package io.bindingz.server.controller;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import io.bindingz.server.code.SourceCodeConfiguration;
import io.bindingz.server.code.SourceCodeFactory;
import io.bindingz.server.model.SchemaDto;
import io.bindingz.server.model.SourceDto;
import io.bindingz.server.repository.ApiKeyRepository;
import io.bindingz.server.repository.SchemaRepository;
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
public class ApiSchemaController {

    private final SchemaRepository repository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiSchemaController(@Autowired SchemaRepository repository,
                               @Autowired ApiKeyRepository apiKeyRepository) {
        this.repository = repository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "/api/v1/schemas", method = {RequestMethod.GET})
    public ResponseEntity<Collection<SchemaResource>> getAll(@RequestHeader("Authorization") String authorization) {
        System.out.println("Getting ...");

        Optional<String> clientId = clientIdentifier(authorization);
        if (clientId.isPresent()) {
            return new ResponseEntity(repository.findAllByClient(clientId.get()), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/api/v1/schemas/{namespace}/{owner}/{contractName}", method = RequestMethod.GET)
    public ResponseEntity<SchemaResource> getSchema(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("namespace") String namespace,
            @PathVariable("owner") String owner,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version) {
        System.out.println("Getting ...");

        Optional<String> clientId = clientIdentifier(authorization);
        if (clientId.isPresent()) {
            Optional<SchemaDto> schemaDto = repository.find(clientId.get(), namespace, owner, contractName, version.orElse("latest"));
            return schemaDto.map(s -> new ResponseEntity<>(new SchemaResource(s), HttpStatus.OK)).
                    orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/api/v1/schemas/{namespace}/{owner}/{contractName}", method = RequestMethod.POST)
    public ResponseEntity<SchemaResource> postSchema(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("namespace") String namespace,
            @PathVariable("owner") String owner,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version,
            @RequestBody JsonSchema schema) {
        System.out.println("Posting ...");

        Optional<String> clientId = clientIdentifier(authorization);
        if (clientId.isPresent()) {
            SchemaDto versioned = new SchemaDto(clientId.get(), namespace, owner, contractName, version.orElse("latest"), schema);
            SchemaDto latest = new SchemaDto(clientId.get(), namespace, owner, contractName, "latest", schema);

            repository.add(versioned);
            repository.add(latest);

            return new ResponseEntity<>(new SchemaResource(versioned), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @RequestMapping(value = "/api/v1/sources/{namespace}/{owner}/{contractName}", method = RequestMethod.POST)
    public ResponseEntity<SourceResource> createSources(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("namespace") String namespace,
            @PathVariable("owner") String owner,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version,
            @RequestBody SourceCodeConfiguration configuration) throws IOException {
        System.out.println("Creating ...");

        Optional<String> clientId = clientIdentifier(authorization);
        if (clientId.isPresent()) {
            Optional<SchemaDto> schemaDto = repository.find(clientId.get(), namespace, owner, contractName, version.orElse("latest"));
            if (schemaDto.isPresent()) {
                List<SourceDto> sources = new SourceCodeFactory().create(schemaDto.get(), configuration);
                return new ResponseEntity<>(new SourceResource(schemaDto.get(), sources), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Optional<String> clientIdentifier(String apiKey) {
        return apiKeyRepository.findClientIdentifier(apiKey);
    }
}
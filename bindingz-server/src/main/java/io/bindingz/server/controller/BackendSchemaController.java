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

import io.bindingz.server.model.ExposedApiKeyDto;
import io.bindingz.server.model.SchemaDto;
import io.bindingz.server.repository.SchemaRepository;
import io.bindingz.server.service.ApiKeyService;
import io.bindingz.server.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
public class BackendSchemaController {

    private final SchemaRepository repository;
    private final ApiKeyService apiKeyService;
    private final JwtService jwtService;

    public BackendSchemaController(@Autowired SchemaRepository repository,
                                   @Autowired ApiKeyService apiKeyService,
                                   @Autowired JwtService jwtService) {
        this.repository = repository;
        this.apiKeyService = apiKeyService;
        this.jwtService = jwtService;
    }

    @RequestMapping(value = "/backend/v1/keys", method = {RequestMethod.POST})
    public ResponseEntity<ExposedApiKeyDto> createApiKey(
            @RequestHeader("Authorization") String token) {
        System.out.println("Creating ...");
        
        return new ResponseEntity(apiKeyService.createApiKey(clientId(token)), HttpStatus.OK);
    }

    @RequestMapping(value = "/backend/v1/keys", method = {RequestMethod.GET})
    public ResponseEntity<ExposedApiKeyDto> getApiKeys(
            @RequestHeader("Authorization") String token) {
        System.out.println("Creating ...");

        return new ResponseEntity(apiKeyService.getApiKeys(clientId(token)), HttpStatus.OK);
    }

    @RequestMapping(value = "/backend/v1/schemas", method = {RequestMethod.GET})
    public ResponseEntity<Collection<SchemaResource>> getAll(
            @RequestHeader("Authorization") String token) {
        System.out.println("Getting ...");

        return new ResponseEntity(repository.findAllByClient(clientId(token)), HttpStatus.OK);
    }

    @RequestMapping(value = "/backend/v1/schemas/{namespace}/{owner}/{contractName}", method = RequestMethod.GET)
    public ResponseEntity<SchemaResource> getSchema(
            @RequestHeader("Authorization") String token,
            @PathVariable("namespace") String namespace,
            @PathVariable("owner") String owner,
            @PathVariable("contractName") String contractName,
            @RequestParam("version") Optional<String> version) {
        System.out.println("Getting ...");

        Optional<SchemaDto> schemaDto = repository.find(clientId(token), namespace, owner, contractName, version.orElse("latest"));
        return schemaDto.map(s -> new ResponseEntity<>(new SchemaResource(s), HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private String clientId(String jwt) {
        return jwtService.getClientId(jwt);
    }
}
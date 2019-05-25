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

package com.monadiccloud.bindingz.contract.registry;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SchemaController {

    private final SchemaRepository repository;

    public SchemaController(@Autowired SchemaRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/api/v1/schemas/{providerName}/{contractName}", method = RequestMethod.POST)
    public ResponseEntity<SchemaResource> post(@RequestBody JsonSchema schema,
                                              @PathVariable("providerName") String providerName,
                                              @PathVariable("contractName") String contractName,
                                              @RequestParam("version") Optional<String> version) {
        System.out.println("Posting ...");

        SchemaDto versioned = new SchemaDto(contractName, providerName, version.orElse("latest"), schema);
        SchemaDto latest = new SchemaDto(contractName, providerName, "latest", schema);

        repository.add(versioned);
        repository.add(latest);

        return new ResponseEntity<SchemaResource>(new SchemaResource(versioned), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/v1/schemas/{providerName}/{contractName}", method = RequestMethod.GET)
    public ResponseEntity<SchemaResource> get(@PathVariable("providerName") String providerName,
                              @PathVariable("contractName") String contractName,
                              @RequestParam("version") Optional<String> version) {
        System.out.println("Getting ...");

        SchemaDto schemaDto = repository.find(contractName, providerName, version.orElse("latest"));
        return new ResponseEntity<SchemaResource>(new SchemaResource(schemaDto), HttpStatus.OK);
    }
}
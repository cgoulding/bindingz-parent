package com.monadiccloud.bindingz.contract.registry;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
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

    private SchemaRepository repository = new SchemaRepository();

    @RequestMapping(value = "/api/v1/schemas/{providerName}/{contractName}", method = RequestMethod.POST)
    public ResponseEntity<SchemaResource> post(@RequestBody JsonSchema schema,
                                              @PathVariable("providerName") String providerName,
                                              @PathVariable("contractName") String contractName,
                                              @RequestParam("version") Optional<String> version) {
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
        SchemaDto schemaDto = repository.find(contractName, providerName, version.orElse("latest"));
        return new ResponseEntity<SchemaResource>(new SchemaResource(schemaDto), HttpStatus.OK);
    }
}
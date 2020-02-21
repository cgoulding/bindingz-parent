package com.monadiccloud.bindingz.contract.registry.repository.inmemory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.registry.model.SchemaDto;
import org.junit.Test;

import java.io.IOException;

public class InMemorySchemaRepositoryTest {
    private InMemorySchemaRepository repository = new InMemorySchemaRepository();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        repository.add(objectMapper.readValue("{\"contractName\":\"SampleDto\",\"providerName\":\"example1\",\"version\":\"v1\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class));
        repository.add(objectMapper.readValue("{\"contractName\":\"SampleDto\",\"providerName\":\"example1\",\"version\":\"v2\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class));
        repository.add(objectMapper.readValue("{\"contractName\":\"SampleDto\",\"providerName\":\"example2\",\"version\":\"v2\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class));

        System.out.println(objectMapper.writeValueAsString(repository.findAllByAccount("asdf")));
    }
}

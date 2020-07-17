package io.bindingz.server.repository.inmemory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bindingz.server.model.SchemaDto;
import org.junit.Test;

import java.io.IOException;

public class InMemorySchemaRepositoryTest {
    private InMemorySchemaRepository repository = new InMemorySchemaRepository();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        repository.add(objectMapper.readValue("{\"clientIdentifier\":\"asdf\",\"contractName\":\"SampleDto\",\"owner\":\"example1\",\"version\":\"v1\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class));
        repository.add(objectMapper.readValue("{\"clientIdentifier\":\"asdf\",\"contractName\":\"SampleDto\",\"owner\":\"example1\",\"version\":\"v2\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class));
        repository.add(objectMapper.readValue("{\"clientIdentifier\":\"asdf\",\"contractName\":\"SampleDto\",\"owner\":\"example2\",\"version\":\"v2\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class));

        System.out.println(objectMapper.writeValueAsString(repository.findAllByClient("asdf")));
    }
}

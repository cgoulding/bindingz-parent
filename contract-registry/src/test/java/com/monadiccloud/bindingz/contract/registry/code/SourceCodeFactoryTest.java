package com.monadiccloud.bindingz.contract.registry.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.registry.resources.SchemaDto;
import com.monadiccloud.bindingz.contract.registry.resources.SourceDto;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SourceCodeFactoryTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        SourceCodeFactory factory = new SourceCodeFactory();
        SchemaDto schemaDto = mapper.readValue("{\"contractName\":\"SampleDto\",\"providerName\":\"example1\",\"version\":\"v1\",\"schema\":{\"type\":\"object\",\"id\":\"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto\",\"properties\":{\"one\":{\"type\":\"string\"}}}}", SchemaDto.class);
        SourceCodeConfiguration configuration = new SourceCodeConfiguration();
        configuration.setClassName("SampleClass");
        configuration.setPackageName("com.something");
        List<SourceDto> sources = factory.create(schemaDto, configuration);
        System.out.println(sources);
    }
}

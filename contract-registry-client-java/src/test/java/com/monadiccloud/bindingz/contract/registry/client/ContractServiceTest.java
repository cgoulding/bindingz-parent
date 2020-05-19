package com.monadiccloud.bindingz.contract.registry.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.core.model.ContractDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class ContractServiceTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void test() throws Exception {
        ContractService service = new ContractService(new ObjectMapper());
        Collection<ContractDto> dtos = service.create(
                this.getClass().getClassLoader(),
                "com.monadiccloud.bindingz.contract.registry.client");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtos));
    }
}

package com.monadiccloud.bindingz.contract.registry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

public class SchemaDto {
    private final String contractName;
    private final String providerName;
    private final String version;
    private final JsonSchema schema;

    @JsonCreator
    public SchemaDto(@JsonProperty("contractName") String contractName,
                     @JsonProperty("providerName") String providerName,
                     @JsonProperty("version") String version,
                     @JsonProperty("schema") JsonSchema schema) {
        this.contractName = contractName;
        this.providerName = providerName;
        this.version = version;
        this.schema = schema;
    }

    public String getContractName() {
        return contractName;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getVersion() {
        return version;
    }

    public JsonSchema getSchema() {
        return schema;
    }
}

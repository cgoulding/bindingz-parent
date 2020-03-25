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

package com.monadiccloud.bindingz.contract.registry.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDto {
    private final String namespace;
    private final String owner;
    private final String contractName;
    private final String version;
    private final JsonSchema schema;

    @JsonCreator
    public ContractDto(@JsonProperty("namespace") String namespace,
                       @JsonProperty("owner") String owner,
                       @JsonProperty("contractName") String contractName,
                       @JsonProperty("version") String version,
                       @JsonProperty("schema") JsonSchema schema) {
        this.namespace = namespace;
        this.owner = owner;
        this.contractName = contractName;
        this.version = version;
        this.schema = schema;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getContractName() {
        return contractName;
    }

    public String getOwner() {
        return owner;
    }

    public String getVersion() {
        return version;
    }

    public JsonSchema getSchema() {
        return schema;
    }
}

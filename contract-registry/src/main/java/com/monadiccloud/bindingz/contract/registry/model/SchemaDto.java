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

package com.monadiccloud.bindingz.contract.registry.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

@JsonIgnoreProperties
public class SchemaDto {
    private final String clientIdentifier;
    private final String namespace;
    private final String contractName;
    private final String owner;
    private final String version;
    private final JsonSchema schema;

    @JsonCreator
    public SchemaDto(@JsonProperty("clientIdentifier") String clientIdentifier,
                     @JsonProperty("namespace") String namespace,
                     @JsonProperty("contractName") String contractName,
                     @JsonProperty("owner") String owner,
                     @JsonProperty("version") String version,
                     @JsonProperty("schema") JsonSchema schema) {
        this.clientIdentifier = clientIdentifier;
        this.namespace = namespace;
        this.contractName = contractName;
        this.owner = owner;
        this.version = version;
        this.schema = schema;
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

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public String getNamespace() {
        return namespace;
    }
}

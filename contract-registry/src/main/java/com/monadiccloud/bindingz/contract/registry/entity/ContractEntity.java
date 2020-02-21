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

package com.monadiccloud.bindingz.contract.registry.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Connor Goulding
 */
@Entity
@Table(name = "CONTRACT")
public class ContractEntity implements Serializable {

    @Id
    private ContractId contractId;

    @Column(name = "JSON_SCHEMA")
    private String schema;

    public ContractEntity() {
    }

    public ContractEntity(ContractId contractId, String schema) {
        this.contractId = contractId;
        this.schema = schema;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public void setContractId(ContractId contractId) {
        this.contractId = contractId;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Embeddable
    public static class ContractId implements Serializable {
        @Column(name = "ACCOUNT_IDENTIFIER")
        private String accountIdentifier;

        @Column(name = "NAMESPACE", length = 128)
        private String namespace;

        @Column(name = "PROVIDER", length = 128)
        private String provider;

        @Column(name = "NAME", length = 128)
        private String name;

        @Column(name = "REVISION", length = 32)
        private String revision;

        public ContractId() {
        }

        public ContractId(String accountIdentifier, String namespace, String provider, String name, String revision) {
            this.accountIdentifier = accountIdentifier;
            this.namespace = namespace;
            this.provider = provider;
            this.name = name;
            this.revision = revision;
        }

        public void setAccountIdentifier(String accountIdentifier) {
            this.accountIdentifier = accountIdentifier;
        }

        public String getAccountIdentifier() {
            return accountIdentifier;
        }

        public String getRevision() {
            return revision;
        }

        public void setRevision(String revision) {
            this.revision = revision;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

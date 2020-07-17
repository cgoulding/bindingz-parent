/*
 * Copyright (c) 2020 Connor Goulding
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

package io.bindingz.server.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

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
        @Column(name = "CLIENT_IDENTIFIER")
        private String clientIdentifier;

        @Column(name = "NAMESPACE", length = 128)
        private String namespace;

        @Column(name = "OWNER", length = 128)
        private String owner;

        @Column(name = "NAME", length = 128)
        private String name;

        @Column(name = "REVISION", length = 32)
        private String revision;

        public ContractId() {
        }

        public ContractId(String clientIdentifier, String namespace, String owner, String name, String revision) {
            this.clientIdentifier = clientIdentifier;
            this.namespace = namespace;
            this.owner = owner;
            this.name = name;
            this.revision = revision;
        }

        public void setClientIdentifier(String clientIdentifier) {
            this.clientIdentifier = clientIdentifier;
        }

        public String getClientIdentifier() {
            return clientIdentifier;
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

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ContractId that = (ContractId) o;
            return Objects.equals(clientIdentifier, that.clientIdentifier) &&
                    Objects.equals(namespace, that.namespace) &&
                    Objects.equals(owner, that.owner) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(revision, that.revision);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clientIdentifier, namespace, owner, name, revision);
        }
    }
}

package com.monadiccloud.bindingz.contract.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SchemaRepository {

    private Map<SchemaKey, SchemaDto> schemas = new HashMap<>();

    public void add(SchemaDto schemaDto) {
        schemas.put(
                new SchemaKey(schemaDto.getContractName(), schemaDto.getProviderName(), schemaDto.getVersion()),
                schemaDto
        );
    }

    public SchemaDto find(String contractName, String providerName, String version) {
        return schemas.get(new SchemaKey(contractName, providerName, version));
    }

    private static class SchemaKey {
        private final String contractName;
        private final String providerName;
        private final String version;

        public SchemaKey(String contractName,
                         String providerName,
                         String version) {
            this.contractName = contractName;
            this.providerName = providerName;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SchemaKey schemaKey = (SchemaKey) o;
            return Objects.equals(contractName, schemaKey.contractName) &&
                    Objects.equals(providerName, schemaKey.providerName) &&
                    Objects.equals(version, schemaKey.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(contractName, providerName, version);
        }
    }
}

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

package com.monadiccloud.bindingz.contract.plugin.maven;

import com.monadiccloud.bindingz.contract.registry.client.configuration.FactoryType;

import java.util.HashMap;
import java.util.Map;

public class ProcessConfiguration {

    private String owner;
    private String contractName;
    private String version;

    private String packageName;
    private String className;

    private FactoryType factoryType = FactoryType.JSONSCHEMA2POJO;
    private Map<String, String> factoryConfiguration = new HashMap<>();

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public FactoryType getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(FactoryType factoryType) {
        this.factoryType = factoryType;
    }

    public Map<String, String> getFactoryConfiguration() {
        return factoryConfiguration;
    }

    public void setFactoryConfiguration(Map<String, String> factoryConfiguration) {
        this.factoryConfiguration = factoryConfiguration;
    }
}

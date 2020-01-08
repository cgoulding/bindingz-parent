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

import com.monadiccloud.bindingz.contract.plugin.maven.resources.ProviderType;

import java.util.HashMap;
import java.util.Map;

public class ProcessConfiguration {

    private String providerName;
    private String contractName;
    private String version;

    private String packageName;
    private String className;

    private ProviderType codeProviderType = ProviderType.JSONSCHEMA2POJO;
    private Map<String, String> codeProviderConfiguration = new HashMap<>();

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public ProviderType getCodeProviderType() {
        return codeProviderType;
    }

    public void setCodeProviderType(ProviderType codeProviderType) {
        this.codeProviderType = codeProviderType;
    }

    public Map<String, String> getCodeProviderConfiguration() {
        return codeProviderConfiguration;
    }

    public void setCodeProviderConfiguration(Map<String, String> codeProviderConfiguration) {
        this.codeProviderConfiguration = codeProviderConfiguration;
    }
}

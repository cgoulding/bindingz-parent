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

package com.monadiccloud.bindingz.contract.plugin.maven.extension;

import java.io.File;

import com.monadiccloud.bindingz.contract.plugin.maven.providers.JsonSchema2PojoConfiguration;
import com.monadiccloud.bindingz.contract.plugin.maven.resources.ProviderType;

public class ProcessConfiguration {

    // naavro config
    private File targetSourceDirectory;
    private File targetResourceDirectory;

    private String name;
    private String providerName;
    private String contractName;
    private String version;

    private String packageName;
    private String className;

    private ProviderType providerType;
    private JsonSchema2PojoConfiguration jsonSchema2PojoConfiguration;

    public void jsonSchema2Pojo() {
        providerType = ProviderType.JSONSCHEMA2POJO;
        jsonSchema2PojoConfiguration = new JsonSchema2PojoConfiguration(
                targetSourceDirectory,
                targetResourceDirectory,
                providerName,
                contractName,
                version,
                packageName,
                className
        );
    }

    public File getTargetSourceDirectory() {
        return targetSourceDirectory;
    }

    public void setTargetSourceDirectory(File targetSourceDirectory) {
        this.targetSourceDirectory = targetSourceDirectory;
    }

    public File getTargetResourceDirectory() {
        return targetResourceDirectory;
    }

    public void setTargetResourceDirectory(File targetResourceDirectory) {
        this.targetResourceDirectory = targetResourceDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public JsonSchema2PojoConfiguration getJsonSchema2PojoConfiguration() {
        return jsonSchema2PojoConfiguration;
    }

    public void setJsonSchema2PojoConfiguration(JsonSchema2PojoConfiguration jsonSchema2PojoConfiguration) {
        this.jsonSchema2PojoConfiguration = jsonSchema2PojoConfiguration;
    }
}

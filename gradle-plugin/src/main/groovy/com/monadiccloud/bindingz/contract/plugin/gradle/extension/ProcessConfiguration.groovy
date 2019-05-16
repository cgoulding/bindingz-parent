package com.monadiccloud.bindingz.contract.plugin.gradle.extension

import com.monadiccloud.bindingz.contract.plugin.gradle.providers.JsonSchema2PojoConfiguration
import com.monadiccloud.bindingz.contract.plugin.gradle.resources.ProviderType

class ProcessConfiguration {

    // naavro config
    File targetSourceDirectory
    File targetResourceDirectory

    String name
    String providerName
    String contractName
    String version

    String packageName
    String className

    ProviderType providerType;
    JsonSchema2PojoConfiguration jsonSchema2PojoConfiguration

    ProcessConfiguration(String name) {
        this.name = name
    }

    def jsonSchema2Pojo(final Closure configureClosure) {
        providerType = ProviderType.JSONSCHEMA2POJO
        jsonSchema2PojoConfiguration = new JsonSchema2PojoConfiguration(
                targetSourceDirectory,
                targetResourceDirectory,
                providerName,
                contractName,
                version,
                packageName,
                className
        )

        configureClosure.delegate = jsonSchema2PojoConfiguration
        configureClosure.call()
    }
}

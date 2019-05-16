package com.monadiccloud.bindingz.contract.plugin.gradle.extension

import org.gradle.api.NamedDomainObjectContainer

class BindingzExtension {
    String broker

    File targetSourceDirectory;
    File targetResourceDirectory;
    File targetDistributionDirectory;

    NamedDomainObjectContainer<ProcessConfiguration> consumerConfigurations
    NamedDomainObjectContainer<PublishConfiguration> producerConfigurations

    BindingzExtension() {
        super()
    }

    def consumerConfigurations(final Closure configureClosure) {
        consumerConfigurations.configure(configureClosure)
    }

    def producerConfigurations(final Closure configureClosure) {
        producerConfigurations.configure(configureClosure)
    }

    @Override
    String toString() {
        """ |broker = ${broker}
            |targetSourceDirectory = ${targetSourceDirectory}
            |targetResourceDirectory = ${targetResourceDirectory}
            |distributionResourceDirectory = ${targetDistributionDirectory}
        """
    }
}

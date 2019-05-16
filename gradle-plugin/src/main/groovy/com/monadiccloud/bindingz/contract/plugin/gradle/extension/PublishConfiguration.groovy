package com.monadiccloud.bindingz.contract.plugin.gradle.extension;

class PublishConfiguration {
    String name
    String scanBasePackage

    PublishConfiguration(String name) {
        this.name = name
    }
}

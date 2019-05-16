package com.monadiccloud.bindingz.contract.plugin.gradle.extension

import org.junit.Assert
import org.junit.Test

class ProcessConfigurationTest {

    @Test
    void test() {
        def dto = new ProcessConfiguration("woop")
        dto.jsonSchema2Pojo {
            className = "WishfulThinking"
        }

        Assert.assertEquals("WishfulThinking", dto.jsonSchema2PojoConfiguration.className)
    }
}

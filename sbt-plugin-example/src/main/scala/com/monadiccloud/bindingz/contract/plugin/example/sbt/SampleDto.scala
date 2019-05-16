package com.monadiccloud.bindingz.contract.plugin.example.sbt

import com.monadiccloud.bindingz.contract.annotations4s.Contract

@Contract(providerName = "sbt-plugin-example", contractName = "SampleDto", version = "v2")
case class SampleDto(val one: String)

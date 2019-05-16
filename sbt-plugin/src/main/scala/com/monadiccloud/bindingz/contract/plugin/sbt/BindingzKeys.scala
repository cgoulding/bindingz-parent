package com.monadiccloud.bindingz.contract.plugin.sbt

import sbt.{File, settingKey, taskKey}

trait BindingzKeys {
  val bindingzBroker = settingKey[String]("Bindingz broker")

  val bindingzTargetSourceDirectory = settingKey[File]("Target source directory")
  val bindingzTargetResourceDirectory = settingKey[File]("Target resource directory")
  val bindingzDistributionResourceDirectory = settingKey[File]("Distribution resource directory")

  val bindingzPublishConfigurations = settingKey[Seq[BindingzPublishConfiguration]]("Bindingz publish configurations")
  val bindingzProcessConfigurations = settingKey[Seq[BindingzProcessConfiguration]]("Bindingz process configurations")

  val bindingzProcessResources = taskKey[Unit]("Process resources")
  val bindingzPublishResources = taskKey[Unit]("Publish contract code")
}

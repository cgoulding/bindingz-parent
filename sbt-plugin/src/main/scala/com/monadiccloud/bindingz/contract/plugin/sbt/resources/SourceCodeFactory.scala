package com.monadiccloud.bindingz.contract.plugin.sbt.resources

import argus.macros.ModelBuilder
import argus.schema.Schema

object SourceCodeFactory {

  private val modelBuilder = new ModelBuilder[scala.reflect.runtime.universe.type](scala.reflect.runtime.universe)

  def create(content: String, contractName: String, packageName: Option[String]) = {
    val schema = Schema.fromJson(content)

    val (_, trees) = modelBuilder.mkSchemaDef(contractName, schema)

    val packageCode = packageName.map(pn => s"package $pn;\n\n").getOrElse("")

    // Clean up the code a little to make it more readable
    packageCode + trees.map(tree => scala.reflect.runtime.universe.showCode(tree)).mkString("\n\n")
  }
}

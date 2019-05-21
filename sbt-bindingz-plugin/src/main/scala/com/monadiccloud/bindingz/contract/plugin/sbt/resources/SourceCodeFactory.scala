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

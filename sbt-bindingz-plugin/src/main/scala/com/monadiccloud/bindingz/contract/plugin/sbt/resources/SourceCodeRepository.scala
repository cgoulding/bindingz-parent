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

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Path, Paths}

class SourceCodeRepository(val buildDir: Path) {

  def save(contractName: String, packageName: Option[String], sourceCode: String) = {
    val directory = Paths.get(buildDir.toString, packageName.getOrElse("").replaceAll("\\.", File.separator))
    Files.createDirectories(directory)

    val output = Paths.get(directory.toString, contractName + ".scala")
    println("Writing generated code to: " + output.toString)

    val writer = new PrintWriter(output.toFile)
    try writer.write(sourceCode)
    finally writer.close()
  }
}

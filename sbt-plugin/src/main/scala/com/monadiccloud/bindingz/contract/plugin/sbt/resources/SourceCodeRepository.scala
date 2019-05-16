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

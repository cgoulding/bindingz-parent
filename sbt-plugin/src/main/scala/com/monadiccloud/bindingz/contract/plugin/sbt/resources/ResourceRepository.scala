package com.monadiccloud.bindingz.contract.plugin.sbt.resources

import java.nio.file.{Files, Path, Paths}
import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import sbt.IO
import scalaj.http.Http

import scala.collection.JavaConverters._

class ResourceRepository(val brokerString: String, val distributionDir: Path) {

  val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  def export(): Unit = {
    Files.walk(distributionDir).collect(Collectors.toList()).asScala.
      filter(f => Files.isRegularFile(f)).
      map(f => mapper.readValue(f.toFile(), classOf[SchemaResourceContent])).
      foreach(r => post(r))
  }

  def save(schemaResource: SchemaResourceContent): Unit = {
    val fileName = Paths.get(
      distributionDir.toString(),
      s"${schemaResource.providerName}-${schemaResource.contractName}-${schemaResource.version}"
    )
    IO.write(fileName.toFile, mapper.writeValueAsString(schemaResource))
  }

  private def post(schemaResource: SchemaResourceContent): String = {
    val url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
      brokerString,
      schemaResource.providerName,
      schemaResource.contractName,
      schemaResource.version)

    val post = Http(url).header("Content-Type", "application/json").postData(
      mapper.writerWithDefaultPrettyPrinter.writeValueAsString(schemaResource.schema)
    ).asString

    println(s"Response $post")
    post.statusLine
  }
}
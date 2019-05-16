package com.monadiccloud.bindingz.contract.plugin.gradle.resources

import com.fasterxml.jackson.databind.ObjectMapper

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ResourceRepository {

  private String brokerString
  private Path distributionDir

  private ObjectMapper mapper = new ObjectMapper()

  ResourceRepository(String brokerString, Path distributionDir) {
    this.brokerString = brokerString
    this.distributionDir = distributionDir
  }

  def export() {
    Files.walk(distributionDir).
      filter{f -> Files.isRegularFile(f)}.
      map{f -> mapper.readValue(f.toFile(), SchemaResourceContent.class)}.
      forEach{r -> post(r)}
  }

  def save(SchemaResourceContent naavroResource) {
    File file = Paths.get(
      distributionDir.toString(),
      """${naavroResource.providerName}-${naavroResource.contractName}-${naavroResource.version}"""
    ).toFile()

    file.write(mapper.writeValueAsString(naavroResource))
  }

  private def post(SchemaResourceContent naavroResource) {
    def url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
      brokerString,
      naavroResource.providerName,
      naavroResource.contractName,
      naavroResource.version)

    // POST
    def post = new URL(url).openConnection()
    def body = mapper.writeValueAsString(naavroResource.schema)

    post.setRequestMethod("POST")
    post.setDoOutput(true)
    post.setRequestProperty("Content-Type", "application/json")
    post.getOutputStream().write(body.getBytes("UTF-8"))
    post.getInputStream().getText()
  }
}
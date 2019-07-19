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

package com.monadiccloud.bindingz.contract.plugin.maven.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceRepository {

  private String registryString;
  private Path distributionDir;

  private ObjectMapper mapper = new ObjectMapper();

  public ResourceRepository(String registryString, Path distributionDir) {
    this.registryString = registryString;
    this.distributionDir = distributionDir;
  }

  public void export() throws IOException {
    Files.walk(distributionDir).
      filter(f -> Files.isRegularFile(f)).
      map(f -> {
        try {
          return mapper.readValue(f.toFile(), SchemaResourceContent.class);
        } catch (IOException e) {
          return new SchemaResourceContent();
        }
      }).
      forEach(r -> post(r));
  }

  public void save(SchemaResourceContent naavroResource) throws IOException {
    File file = Paths.get(
      distributionDir.toString(),
      String.format("%s-%s-%s", naavroResource.providerName, naavroResource.contractName, naavroResource.version)
    ).toFile();

    mapper.writeValue(file, naavroResource);
  }

  private void post(SchemaResourceContent naavroResource) {
    String url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
      registryString,
      naavroResource.providerName,
      naavroResource.contractName,
      naavroResource.version);

    // POST
    try {
      URLConnection post = new URL(url).openConnection();
      String body = mapper.writeValueAsString(naavroResource.schema);

      post.setDoOutput(true);
      post.setRequestProperty("Content-Type", "application/json");
      post.getOutputStream().write(body.getBytes("UTF-8"));
      post.connect();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SchemaRepository {

  private String registryString;
  private Path distributionDir;

  private ObjectMapper mapper = new ObjectMapper();

  public SchemaRepository(String registryString, Path distributionDir) {
    this.registryString = registryString;
    this.distributionDir = distributionDir;
  }

  public void export() throws IOException {
    Files.walk(distributionDir).
      filter(f -> Files.isRegularFile(f)).
      flatMap(f -> {
        try {
          return Stream.of(mapper.readValue(f.toFile(), SchemaDto.class));
        } catch (IOException e) {
          return Stream.empty();
        }
      }).
      forEach(r -> post(r));
  }

  public void save(SchemaDto schemaDto) throws IOException {
    File file = Paths.get(
      distributionDir.toString(),
      String.format("%s-%s-%s", schemaDto.getProviderName(), schemaDto.getContractName(), schemaDto.getVersion())
    ).toFile();

    mapper.writeValue(file, schemaDto);
  }

  private String post(SchemaDto schemaDto) {
    String url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
      registryString,
      schemaDto.getProviderName(),
      schemaDto.getContractName(),
      schemaDto.getVersion());

    // POST
    try {
      HttpURLConnection post = (HttpURLConnection)new URL(url).openConnection();
      post.setDoOutput(true);
      post.setRequestProperty("Content-Type", "application/json");
      post.setRequestMethod("POST");
      post.connect();

      String body = mapper.writeValueAsString(schemaDto.getSchema());
      post.getOutputStream().write(body.getBytes("UTF-8"));

      int responseCode = post.getResponseCode();
      System.out.println("\nSending 'POST' request to URL : " + url);
      System.out.println("Response Code : " + responseCode);

      try (BufferedReader in = new BufferedReader(new InputStreamReader(post.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String line = null;
        while ((line = in.readLine()) != null) {
          response.append(line);
        }
        return response.toString();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

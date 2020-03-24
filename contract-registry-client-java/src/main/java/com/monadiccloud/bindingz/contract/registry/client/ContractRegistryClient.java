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

package com.monadiccloud.bindingz.contract.registry.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monadiccloud.bindingz.contract.registry.client.configuration.SourceCodeConfiguration;
import com.monadiccloud.bindingz.contract.registry.client.model.ContractDto;
import com.monadiccloud.bindingz.contract.registry.client.model.ContractResource;
import com.monadiccloud.bindingz.contract.registry.client.model.SourceResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ContractRegistryClient {

  private String registryString;

  private ObjectMapper mapper = new ObjectMapper();

  public ContractRegistryClient(String registryString) {
    this.registryString = registryString;
  }

  public ContractResource publishContract(ContractDto schemaDto) {
    String url = String.format("%s/api/v1/schemas/%s/%s?version=%s",
      registryString,
      schemaDto.getOwner(),
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
        return mapper.readValue(response.toString(), ContractResource.class);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public SourceResource generateSources(String owner,
                                        String contractName,
                                        String version,
                                        SourceCodeConfiguration configuration) {
    String url = String.format("%s/api/v1/sources/%s/%s?version=%s",
            registryString,
            owner,
            contractName,
            version);

    // POST
    try {
      HttpURLConnection post = (HttpURLConnection)new URL(url).openConnection();
      post.setDoOutput(true);
      post.setRequestProperty("Content-Type", "application/json");
      post.setRequestMethod("POST");
      post.connect();

      String body = mapper.writeValueAsString(configuration);
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
        return mapper.readValue(response.toString(), SourceResource.class);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.monadiccloud.bindingz.contract.annotations4j.Contract;
import dorkbox.annotation.AnnotationDefaults;
import dorkbox.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.stream.Collectors;

public class ResourceFactory {

  ObjectMapper mapper = new ObjectMapper();

  Collection<SchemaResourceContent> create(ClassLoader classLoader, String... packageNames) throws IOException {
    Collection<Class<?>> contractClasses = AnnotationDetector.
      scanClassPath(classLoader, packageNames).
      forAnnotations(Contract.class).
      on(ElementType.TYPE).
      collect(AnnotationDefaults.getType);

    return contractClasses.stream().map(clazz -> createResource(clazz)).collect(Collectors.toList());
  }

  private SchemaResourceContent createResource(Class contract) {
    JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
    JsonSchema schema = null;
    try {
      schema = schemaGen.generateSchema(contract);
      Contract contractProvider = (Contract)contract.getAnnotation(Contract.class);

      SchemaResourceContent naavroResource = new SchemaResourceContent();
      naavroResource.contractName = contractProvider.contractName();
      naavroResource.providerName = contractProvider.providerName();
      naavroResource.version = contractProvider.version();
      naavroResource.schema = schema;
      return naavroResource;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    }
  }
}

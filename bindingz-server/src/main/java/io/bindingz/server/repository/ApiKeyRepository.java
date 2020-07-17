/*
 * Copyright (c) 2019. Connor Goulding
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

package io.bindingz.server.repository;

import io.bindingz.server.RegistryException;
import io.bindingz.server.model.HashedApiKeyDto;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository {
    List<HashedApiKeyDto> getApiKeys(String clientIdentifier) throws RegistryException;
    Optional<String> findClientIdentifier(String apiKey) throws RegistryException;
    void saveApiKey(HashedApiKeyDto apiKey) throws RegistryException;
}

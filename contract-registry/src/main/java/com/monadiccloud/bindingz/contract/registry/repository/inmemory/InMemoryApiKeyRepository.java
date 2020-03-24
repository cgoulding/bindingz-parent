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
package com.monadiccloud.bindingz.contract.registry.repository.inmemory;

import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.model.HashedApiKeyDto;
import com.monadiccloud.bindingz.contract.registry.repository.ApiKeyRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Profile({"inmemory"})
public class InMemoryApiKeyRepository implements ApiKeyRepository {
    private final Map<String, HashedApiKeyDto> LOOKUP = new HashMap<>();

    @Override
    public Optional<String> findClientIdentifier(String apiKey) throws RegistryException {
        return Optional.ofNullable(LOOKUP.get(apiKey)).map(HashedApiKeyDto::getClientIdentifier);
    }

    @Override
    public void saveApiKey(HashedApiKeyDto apiKey) throws RegistryException {
        LOOKUP.put(apiKey.getHash(), apiKey);
    }

    @Override
    public List<HashedApiKeyDto> getApiKeys(String clientIdentifier) {
        return LOOKUP.values().stream().
                filter(x -> x.getClientIdentifier().equals(clientIdentifier)).
                collect(Collectors.toList());
    }
}

/*
 * Copyright (c) 2020 Connor Goulding
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
package io.bindingz.server.repository.jpa;

import io.bindingz.server.RegistryException;
import io.bindingz.server.dao.JpaApiKeyDao;
import io.bindingz.server.entity.ApiKeyEntity;
import io.bindingz.server.model.HashedApiKeyDto;
import io.bindingz.server.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Profile({"prod"})
public class JpaApiKeyRepository implements ApiKeyRepository {

    private final JpaApiKeyDao apiKeyDao;

    public JpaApiKeyRepository(@Autowired JpaApiKeyDao apiKeyDao) {
        this.apiKeyDao = apiKeyDao;
    }

    @Override
    public Optional<String> findClientIdentifier(String apiKey) throws RegistryException {
        return apiKeyDao.findById(apiKey).map(ApiKeyEntity::getClientIdentifier);
    }

    @Override
    public void saveApiKey(HashedApiKeyDto apiKey) throws RegistryException {
        apiKeyDao.save(new ApiKeyEntity(
                apiKey.getPrefix(),
                apiKey.getHash(),
                apiKey.getClientIdentifier(),
                apiKey.getExpiry()
        ));
    }

    @Override
    public List<HashedApiKeyDto> getApiKeys(String clientIdentifier) {
        return apiKeyDao.findByClientIdentifier(clientIdentifier).stream().
                map(this::transform).
                collect(Collectors.toList());
    }

    private HashedApiKeyDto transform(ApiKeyEntity apiKeyEntity) {
        return new HashedApiKeyDto(
                apiKeyEntity.getClientIdentifier(),
                apiKeyEntity.getPrefix(),
                apiKeyEntity.getApiKey(),
                apiKeyEntity.getExpiryDate()
        );
    }
}

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
package com.monadiccloud.bindingz.contract.registry.repository.jpa;

import com.monadiccloud.bindingz.contract.registry.RegistryException;
import com.monadiccloud.bindingz.contract.registry.dao.JpaApiKeyDao;
import com.monadiccloud.bindingz.contract.registry.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"lambda", "prod"})
public class JpaApiKeyRepository implements ApiKeyRepository {

    private final JpaApiKeyDao apiKeyDao;

    public JpaApiKeyRepository(@Autowired JpaApiKeyDao apiKeyDao) {
        this.apiKeyDao = apiKeyDao;
    }

    @Override
    public String findAccountIdentifier(String apiKey) throws RegistryException {
        return apiKeyDao.findOne(apiKey).getAccountIdentifier();
    }
}

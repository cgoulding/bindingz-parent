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

package com.monadiccloud.bindingz.contract.registry.service;

import com.monadiccloud.bindingz.contract.registry.model.ExposedApiKeyDto;
import com.monadiccloud.bindingz.contract.registry.model.HashedApiKeyDto;
import com.monadiccloud.bindingz.contract.registry.repository.ApiKeyRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ApiKeyService {
    private static MessageDigest MD5;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<HashedApiKeyDto> getApiKeys(String clientIdentifier) {
        return apiKeyRepository.getApiKeys(clientIdentifier);
    }

    public ExposedApiKeyDto createApiKey(String clientIdentifier) {
        ExposedApiKeyDto exposed = create(clientIdentifier);
        HashedApiKeyDto hashed = transform(exposed);
        apiKeyRepository.saveApiKey(hashed);
        return exposed;
    }

    private ExposedApiKeyDto create(String clientIdentifier) {
        String prefix = RandomStringUtils.randomAlphanumeric(8);
        String key = hash(UUID.randomUUID().toString().getBytes());

        return new ExposedApiKeyDto(
                clientIdentifier,
                prefix,
                key
        );
    }

    private HashedApiKeyDto transform(ExposedApiKeyDto key) {
        return new HashedApiKeyDto(
                key.getClientIdentifier(),
                key.getPrefix(),
                hash(clientKey(key.getPrefix(), key.getKey()).getBytes()),
                Date.from(LocalDateTime.now().plusYears(1).toInstant(ZoneOffset.UTC))
        );
    }

    private String hash(byte[] input) {
        return new BigInteger(1, MD5.digest(input)).toString(16);
    }

    private String clientKey(String prefix, String key) {
        return prefix + "." + key;
    }
}

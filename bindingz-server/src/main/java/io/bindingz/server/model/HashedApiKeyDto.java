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

package io.bindingz.server.model;

import java.util.Date;

public class HashedApiKeyDto {
    private final String clientIdentifier;
    private final String prefix;
    private final String hash;
    private final Date expiry;

    public HashedApiKeyDto(String clientIdentifier, String prefix, String hash, Date expiry) {
        this.clientIdentifier = clientIdentifier;
        this.prefix = prefix;
        this.hash = hash;
        this.expiry = expiry;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getHash() {
        return hash;
    }

    public Date getExpiry() {
        return expiry;
    }
}

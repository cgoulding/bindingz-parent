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
package io.bindingz.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "API_KEY")
public class ApiKeyEntity {

    @Id
    @Column(name = "API_KEY")
    private String apiKey;

    @Column(name = "PREFIX")
    private String prefix;

    @Column(name = "CLIENT_IDENTIFIER")
    private String clientIdentifier;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    public ApiKeyEntity() {
    }

    public ApiKeyEntity(String prefix, String apiKey, String clientIdentifier, Date expiryDate) {
        this.prefix = prefix;
        this.apiKey = apiKey;
        this.clientIdentifier = clientIdentifier;
        this.expiryDate = expiryDate;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}

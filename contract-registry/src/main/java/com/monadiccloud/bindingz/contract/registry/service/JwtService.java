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

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
//    private static final String REGION = "eu-west-1";
//    private static final String POOL_ID = "eu-west-1:13496dc4-95c0-4555-ba34-3f67453f65ce";

    public String getClientId(String jwt) throws JWTDecodeException {
        // @TODO Need to validation against Cognito
        // @TODO And check claims. Need to add intercept lambda during token creation
        String token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
        return JWT.decode(token).getSubject();
    }
}

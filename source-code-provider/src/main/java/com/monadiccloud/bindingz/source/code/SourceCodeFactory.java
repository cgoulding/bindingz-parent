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

package com.monadiccloud.bindingz.source.code;

import com.monadiccloud.bindingz.contract.core.configuration.SourceCodeConfiguration;
import com.monadiccloud.bindingz.contract.core.model.ContractDto;
import com.monadiccloud.bindingz.contract.core.model.SourceDto;
import com.monadiccloud.bindingz.source.code.provider.jsonschematopojo.JsonSchema2PojoProvider;

import java.util.List;

public class SourceCodeFactory {

    public List<SourceDto> create(ContractDto contractDto, SourceCodeConfiguration configuration) throws SourceCodeProviderException {
        switch (SourceCodeProviderType.valueOf(configuration.getSourceCodeProvider())) {
            case JSONSCHEMA2POJO: {
                return new JsonSchema2PojoProvider().create(contractDto, configuration);
            }
            default:
                throw new UnsupportedOperationException("Unknown provider type: " + configuration.getSourceCodeProvider());
        }
    }
}

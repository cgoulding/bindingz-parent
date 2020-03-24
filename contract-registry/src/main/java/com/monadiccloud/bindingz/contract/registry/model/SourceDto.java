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

package com.monadiccloud.bindingz.contract.registry.model;

import java.util.List;

public class SourceDto {
    private final List<String> file;
    private final String content;

    public SourceDto(List<String> file, String content) {
        this.file = file;
        this.content = content;
    }

    public List<String> getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }
}

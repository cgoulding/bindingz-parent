package com.monadiccloud.bindingz.contract.registry.resources;

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

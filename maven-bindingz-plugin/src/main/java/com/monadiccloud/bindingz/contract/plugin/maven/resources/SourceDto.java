package com.monadiccloud.bindingz.contract.plugin.maven.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties
public class SourceDto {

    private final List<String> file;
    private final String content;

    @JsonCreator
    public SourceDto(@JsonProperty("file") List<String> file,
                     @JsonProperty("content") String content) {
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

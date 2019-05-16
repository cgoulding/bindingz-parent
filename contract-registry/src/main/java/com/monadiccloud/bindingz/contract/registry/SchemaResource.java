package com.monadiccloud.bindingz.contract.registry;

public class SchemaResource {

    private final SchemaDto content;

    public SchemaResource(SchemaDto content) {
        this.content = content;
    }

    public SchemaDto getContent() {
        return content;
    }
}
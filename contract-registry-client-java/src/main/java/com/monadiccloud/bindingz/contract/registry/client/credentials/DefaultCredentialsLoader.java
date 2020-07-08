package com.monadiccloud.bindingz.contract.registry.client.credentials;

import java.util.Optional;

public class DefaultCredentialsLoader implements ApiCredentialsLoader {
    @Override
    public Optional<String> apiKey() {
        return Optional.empty();
    }

    @Override
    public Optional<String> hostname() {
        return Optional.of("https://api.bindingz.io");
    }
}

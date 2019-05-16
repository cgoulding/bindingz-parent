package com.monadiccloud.bindingz.contract.plugin.example.gradle;

import com.monadiccloud.bindingz.contract.annotations4j.Contract;

import java.util.List;

@Contract(contractName = "FooBarEvent", providerName = "gradle-plugin-example", version = "1.0")
public class FooBarEvent {
    private String id;
    private String name;
    private List<Bar> bars;

    public FooBarEvent(String id,
                                String name,
                                List<Bar> bars) {
        this.id = id;
        this.name = name;
        this.bars = bars;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public static class Bar {
        private final String barId;

        public Bar(String barId) {
            this.barId = barId;
        }

        public String getBarId() {
            return barId;
        }
    }
}

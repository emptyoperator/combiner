package org.combiner.kafka.model;

import java.util.HashSet;
import java.util.Set;

public record BrokerDetails(String name, String bootstrapServer, Set<String> consumers, Set<String> producers) {
    public BrokerDetails(String name, String bootstrapServer) {
        this(name, bootstrapServer, new HashSet<>(), new HashSet<>());
    }
}

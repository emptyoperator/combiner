package org.combiner.kafka.model;

import org.combiner.kafka.BrokerClient;

public record Broker(BrokerDetails details, BrokerClient client) {
    public Broker(BrokerDetails details) {
        this(details, new BrokerClient(details.bootstrapServer()));
    }
}

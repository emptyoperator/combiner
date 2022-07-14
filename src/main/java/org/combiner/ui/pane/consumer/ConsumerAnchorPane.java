package org.combiner.ui.pane.consumer;

import org.combiner.kafka.model.Broker;
import org.combiner.ui.pane.GenericAnchorPane;

public class ConsumerAnchorPane extends GenericAnchorPane {
    public ConsumerAnchorPane(Broker broker) {
        super(new ConsumerTabPane(broker));
    }
}

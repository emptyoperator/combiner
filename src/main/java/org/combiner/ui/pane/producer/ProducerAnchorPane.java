package org.combiner.ui.pane.producer;

import org.combiner.kafka.model.Broker;
import org.combiner.ui.pane.GenericAnchorPane;

public class ProducerAnchorPane extends GenericAnchorPane {
    public ProducerAnchorPane(Broker broker) {
        super(new ProducerTabPane(broker));
    }
}

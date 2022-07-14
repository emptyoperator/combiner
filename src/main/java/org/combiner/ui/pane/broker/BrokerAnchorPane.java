package org.combiner.ui.pane.broker;

import org.combiner.kafka.model.Broker;
import org.combiner.ui.pane.GenericAnchorPane;

import java.util.Collection;

public class BrokerAnchorPane extends GenericAnchorPane {
    public BrokerAnchorPane(Collection<Broker> brokers) {
        super(new BrokerTabPane(brokers));
    }
}

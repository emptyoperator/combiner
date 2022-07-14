package org.combiner.ui.pane.broker;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.combiner.kafka.model.Broker;
import org.combiner.ui.pane.GenericTabPane;
import org.combiner.ui.pane.consumer.ConsumerAnchorPane;
import org.combiner.ui.pane.producer.ProducerAnchorPane;

import java.util.Collection;

public class BrokerTabPane extends GenericTabPane {
    private static final String NEW_BROKER = "New broker";

    public BrokerTabPane(Collection<Broker> brokers) {
        setTabs(brokers, broker -> {
            AnchorPane producers = new ProducerAnchorPane(broker);
            AnchorPane consumers = new ConsumerAnchorPane(broker);
            SplitPane splitPane = new SplitPane(producers, consumers);
            splitPane.setDividerPosition(1, 0.5);
            return new Tab(broker.details().name(), splitPane);
        });
        addNewTabButton(() -> new Tab(NEW_BROKER));
    }
}

package org.combiner.ui.pane.producer;

import javafx.scene.control.Tab;
import org.combiner.kafka.model.Broker;
import org.combiner.ui.Views;
import org.combiner.ui.pane.GenericTabPane;

public class ProducerTabPane extends GenericTabPane {
    private static final String NEW_PRODUCER = "New producer";

    public ProducerTabPane(Broker broker) {
        setTabs(broker.details().producers(), topic -> new Tab(topic, Views.producer(topic, broker.client())));
        addNewTabButton(() -> {
            Tab tab = new Tab(NEW_PRODUCER);
            tab.setContent(Views.topicSelector(broker.client(), topic -> {
                tab.setClosable(true);
                tab.setText(topic);
                tab.setContent(Views.producer(topic, broker.client()));
            }));
            return tab;
        });
    }
}

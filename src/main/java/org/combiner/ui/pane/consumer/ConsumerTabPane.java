package org.combiner.ui.pane.consumer;

import javafx.scene.control.Tab;
import org.combiner.kafka.model.Broker;
import org.combiner.ui.Views;
import org.combiner.ui.pane.GenericTabPane;

public class ConsumerTabPane extends GenericTabPane {
    private static final String NEW_CONSUMER = "New consumer";

    public ConsumerTabPane(Broker broker) {
        setTabs(broker.details().consumers(), topic -> new Tab(topic, Views.consumer(topic, broker.client())));
        addNewTabButton(() -> {
            Tab tab = new Tab(NEW_CONSUMER);
            tab.setContent(Views.topicSelector(broker.client(), topic -> {
                getTabByName(topic).ifPresentOrElse(getSelectionModel()::select, () -> {
                    tab.setText(topic);
                    tab.setContent(Views.consumer(topic, broker.client()));
                    tab.setOnClosed(observable -> broker.client().unsubscribe(topic));
                });
            }));
            return tab;
        });
    }
}

package org.combiner.ui.pane.consumer;

import javafx.scene.control.Tab;
import org.combiner.kafka.model.Broker;
import org.combiner.ui.Views;
import org.combiner.ui.pane.GenericTabPane;

public class ConsumerTabPane extends GenericTabPane {
    private static final String NEW_CONSUMER = "New consumer";

    public ConsumerTabPane(Broker broker) {
        setTabs(broker.details().consumers(), topic -> {
            Tab tab = new Tab(topic);
            tab.setContent(Views.consumer(widthProperty(), topic, broker.client()));
            return tab;
        });
        addNewTabButton(() -> {
            Tab tab = new Tab(NEW_CONSUMER);
            tab.setContent(Views.topicSelector(broker.client(), topic -> {
                getTabByName(topic).ifPresentOrElse(getSelectionModel()::select, () -> {
                    tab.setClosable(true);
                    tab.setText(topic);
                    tab.setContent(Views.consumer(widthProperty(), topic, broker.client()));
                    tab.onClosedProperty().addListener(observable -> broker.client().unsubscribe(topic));
                });
            }));
            return tab;
        });
    }
}

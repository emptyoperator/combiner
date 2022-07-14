package org.combiner.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.combiner.event.Events;
import org.combiner.kafka.BrokerClient;
import org.combiner.kafka.model.Broker;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Components {
    private static final String NEW_BROKER = "New broker";
    private static final String NEW_PRODUCER = "New producer";
    private static final String NEW_CONSUMER = "New consumer";

    public static Parent root(Collection<Broker> brokers) {
        return brokersTabPane(brokers);
    }

    private static AnchorPane brokersTabPane(Collection<Broker> brokers) {
        return anchorPane(Tabs.tabPane(Tabs.toTabs(brokers, broker -> new Tab(broker.details().name(), splitPane(broker))), () -> new Tab(NEW_BROKER)));
    }

    private static AnchorPane producersTabPane(Collection<Tab> tabs, BrokerClient client) {
        TabPane tabPane = Tabs.tabPane(tabs, () -> {
            String tabId = UUID.randomUUID().toString();
            Tab tab = new Tab(NEW_PRODUCER, Views.topicSelector(tabId, client));
            tab.setId(tabId);
            return tab;
        });
        tabPane.addEventHandler(Events.TOPIC_SELECTED, e -> Tabs.getTabById(tabPane, e.getTabId()).ifPresent(tab -> {
            tab.setClosable(true);
            tab.setText(e.getTopic());
            tab.setContent(Views.producer(e.getTopic(), client));
        }));
        return anchorPane(tabPane);
    }

    private static AnchorPane consumersTabPane(Collection<Tab> tabs, BrokerClient client) {
        TabPane tabPane = Tabs.tabPane(tabs, () -> {
            String tabId = UUID.randomUUID().toString();
            Tab tab = new Tab(NEW_CONSUMER, Views.topicSelector(tabId, client));
            tab.setId(tabId);
            return tab;
        });
        tabPane.addEventHandler(Events.TOPIC_SELECTED, e -> Tabs.getTabById(tabPane, e.getTabId()).ifPresent(tab -> {
            Tabs.getTabByName(tabPane, e.getTopic()).ifPresentOrElse(tabPane.getSelectionModel()::select, () -> {
                tab.setClosable(true);
                tab.setText(e.getTopic());
                tab.setContent(Views.consumer(tabPane.widthProperty(), e.getTopic(), client));
                tab.onClosedProperty().addListener(observable -> client.unsubscribe(e.getTopic()));
            });
        }));
        return anchorPane(tabPane);
    }

    private static SplitPane splitPane(Broker broker) {
        List<Tab> producersTabs = Tabs.toTabs(
                broker.details().producers(),
                topic -> new Tab(topic, Views.producer(topic, broker.client()))
        );
        List<Tab> consumersTabs = Tabs.toTabs(
                broker.details().consumers(),
                topic -> {
                    Tab tab = new Tab(topic);
                    tab.setContent(Views.consumer(tab.getTabPane().widthProperty(), topic, broker.client()));
                    return tab;
                }
        );
        Node producers = producersTabPane(producersTabs, broker.client());
        Node consumers = consumersTabPane(consumersTabs, broker.client());
        SplitPane splitPane = new SplitPane(producers, consumers);
        splitPane.setDividerPosition(1, 0.5);
        return splitPane;
    }

    private static AnchorPane anchorPane(Node node) {
        return new AnchorPane(setAnchors(node));
    }

    private static Node setAnchors(Node node) {
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        return node;
    }
}

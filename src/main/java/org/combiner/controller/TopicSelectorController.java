package org.combiner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.combiner.event.TopicSelectedEvent;
import org.combiner.kafka.BrokerClient;

public class TopicSelectorController {
    private final String tabId;
    private final BrokerClient client;

    @FXML
    private ChoiceBox<String> topic;

    @FXML
    private Button button;

    public TopicSelectorController(String tabId, BrokerClient client) {
        this.tabId = tabId;
        this.client = client;
    }

    @FXML
    private void initialize() {
        client.topics().thenApply(topics -> topic.getItems().addAll(topics));
        button.setOnAction(e -> button.fireEvent(new TopicSelectedEvent(tabId, topic.getValue())));
    }
}

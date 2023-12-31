package org.combiner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import org.combiner.kafka.BrokerClient;

import java.util.function.Consumer;

public class TopicSelectorController {
    private final BrokerClient client;
    private final Consumer<String> topicConsumer;

    @FXML
    private ChoiceBox<String> topic;

    public TopicSelectorController(BrokerClient client, Consumer<String> topicConsumer) {
        this.client = client;
        this.topicConsumer = topicConsumer;
    }

    @FXML
    private void initialize() {
        client.topics().thenApply(topics -> topic.getItems().addAll(topics));
        topic.setOnAction(e -> topicConsumer.accept(topic.getValue()));
    }
}

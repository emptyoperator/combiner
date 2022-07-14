package org.combiner.controller;

import javafx.application.Platform;
import javafx.beans.binding.NumberExpression;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.combiner.kafka.BrokerClient;
import org.combiner.ui.Views;

public class ConsumerController {
    private final NumberExpression expression;
    private final String topic;
    private final BrokerClient client;

    @FXML
    private ListView<ConsumerRecord<String, String>> records;

    public ConsumerController(NumberExpression expression, String topic, BrokerClient client) {
        this.expression = expression;
        this.topic = topic;
        this.client = client;
    }

    @FXML
    private void initialize() {
        records.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ConsumerRecord<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(Views.record(expression, item));
                }
            }
        });
        client.subscribe(topic, record -> Platform.runLater(() -> records.getItems().add(record)));
    }
}

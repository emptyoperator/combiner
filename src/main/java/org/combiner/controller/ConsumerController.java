package org.combiner.controller;

import javafx.application.Platform;
import javafx.beans.binding.NumberExpression;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.combiner.kafka.BrokerClient;
import org.combiner.ui.NoSelectionModel;
import org.combiner.ui.Views;

public class ConsumerController {
    private final NumberExpression expression;
    private final String topic;
    private final BrokerClient client;

    @FXML
    private ListView<Node> records;

    public ConsumerController(NumberExpression expression, String topic, BrokerClient client) {
        this.expression = expression;
        this.topic = topic;
        this.client = client;
    }

    @FXML
    private void initialize() {
        records.setSelectionModel(new NoSelectionModel<>());
        records.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(item);
                }
            }
        });
        client.subscribe(topic, record -> Platform.runLater(() -> records.getItems().add(Views.record(expression, record))));
    }
}

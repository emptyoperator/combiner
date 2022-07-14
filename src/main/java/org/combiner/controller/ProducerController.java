package org.combiner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.combiner.json.Json;
import org.combiner.kafka.BrokerClient;

public class ProducerController {
    private final String topic;
    private final BrokerClient client;

    @FXML
    private TextField key;

    @FXML
    private TextArea value;

    @FXML
    private Button button;

    public ProducerController(String topic, BrokerClient client) {
        this.topic = topic;
        this.client = client;
    }

    @FXML
    private void initialize() {
        button.setOnAction(e -> client.send(topic, key.getText(), Json.minify(value.getText())));
    }
}

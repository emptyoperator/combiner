package org.combiner.controller;

import javafx.beans.binding.NumberExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RecordController {
    private final NumberExpression expression;
    private final ConsumerRecord<String, String> record;

    @FXML
    private VBox recordInfo;

    @FXML
    private Text timestamp;

    @FXML
    private Text key;

    @FXML
    private Text value;

    @FXML
    private Button button;

    public RecordController(NumberExpression expression, ConsumerRecord<String, String> record) {
        this.expression = expression;
        this.record = record;
    }

    @FXML
    private void initialize() {
        Instant instant = Instant.ofEpochMilli(record.timestamp());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss ").withZone(ZoneId.systemDefault());
        timestamp.setText(formatter.format(instant));
        key.setText(record.key());
        value.setText(record.value());
        recordInfo.maxWidthProperty().bind(expression.subtract(button.widthProperty()));
        value.wrappingWidthProperty().bind(expression.subtract(button.widthProperty()));
        HBox.setHgrow(recordInfo, Priority.ALWAYS);
    }
}

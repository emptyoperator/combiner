package org.combiner.ui;

import javafx.beans.binding.NumberExpression;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.combiner.controller.ConsumerController;
import org.combiner.controller.ProducerController;
import org.combiner.controller.RecordController;
import org.combiner.controller.TopicSelectorController;
import org.combiner.kafka.BrokerClient;

import java.io.IOException;

public class Views {
    public static Node topicSelector(String tabId, BrokerClient client) {
        return loadView("/org/combiner/topic-selector.fxml", new TopicSelectorController(tabId, client));
    }

    public static Node producer(String topic, BrokerClient client) {
        return loadView("/org/combiner/producer.fxml", new ProducerController(topic, client));
    }

    public static Node consumer(NumberExpression expression, String topic, BrokerClient client) {
        return loadView("/org/combiner/consumer.fxml", new ConsumerController(expression, topic, client));
    }

    public static Node record(NumberExpression expression, ConsumerRecord<String, String> record) {
        return loadView("/org/combiner/record.fxml", new RecordController(expression, record));
    }

    private static Node loadView(String name, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(Views.class.getResource(name));
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

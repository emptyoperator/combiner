package org.combiner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.combiner.kafka.BrokerClient;
import org.combiner.kafka.model.Broker;
import org.combiner.kafka.model.BrokerDetails;
import org.combiner.ui.Components;

import java.util.List;

public class CombinerApplication extends Application {
    private static final String APPLICATION_NAME = "Combiner";
    private static final List<BrokerDetails> DETAILS = List.of(new BrokerDetails("Octarine", "localhost:29092"));
    private static final List<Broker> BROKERS = DETAILS.stream().map(Broker::new).toList();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(Components.root(BROKERS), 960, 640);
        stage.setTitle(APPLICATION_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        BROKERS.stream().map(Broker::client).forEach(BrokerClient::close);
    }
}

module org.combiner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires kafka.clients;
    requires com.google.gson;

    opens org.combiner to javafx.fxml;
    exports org.combiner;
    opens org.combiner.event to javafx.fxml;
    exports org.combiner.event;
    opens org.combiner.controller to javafx.fxml;
    exports org.combiner.controller;
    exports org.combiner.kafka;
    exports org.combiner.kafka.model;
    opens org.combiner.kafka.model to javafx.fxml;
    exports org.combiner.ui;
    opens org.combiner.ui to javafx.fxml;
}

package org.combiner.event;

import javafx.event.EventType;

public interface Events {
    EventType<TopicSelectedEvent> TOPIC_SELECTED = new EventType<>("TOPIC_SELECTED");
}

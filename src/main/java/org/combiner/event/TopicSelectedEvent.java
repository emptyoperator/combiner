package org.combiner.event;

import javafx.event.Event;

public class TopicSelectedEvent extends Event {
    private final String tabId;
    private final String topic;

    public TopicSelectedEvent(String tabId, String topic) {
        super(Events.TOPIC_SELECTED);
        this.tabId = tabId;
        this.topic = topic;
    }

    public String getTabId() {
        return tabId;
    }

    public String getTopic() {
        return topic;
    }
}

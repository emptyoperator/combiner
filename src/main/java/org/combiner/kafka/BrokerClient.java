package org.combiner.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

public class BrokerClient implements AutoCloseable {
    private static final String CLIENT_ID = "combiner";
    private static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private static final String VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private static final String THREAD_NAME = "combiner-worker";

    private final Admin admin;
    private final KafkaProducer<String, String> producer;
    private final Map<String, Consumer<ConsumerRecord<String, String>>> listeners = new ConcurrentHashMap<>();
    private final AtomicBoolean running = new AtomicBoolean();
    private final Properties properties = new Properties();

    private KafkaConsumer<String, String> consumer;
    private Thread worker;

    public BrokerClient(String bootstrapServer) {
        properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(CLIENT_ID_CONFIG, CLIENT_ID);
        properties.put(GROUP_ID_CONFIG, CLIENT_ID);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, VALUE_DESERIALIZER);
        admin = Admin.create(properties);
        producer = new KafkaProducer<>(properties);
    }

    public KafkaFuture<Set<String>> topics() {
        return admin.listTopics().names();
    }

    public void send(String topic, String key, String value) {
        producer.send(new ProducerRecord<>(topic, key, value));
    }

    public void subscribe(String topic, Consumer<ConsumerRecord<String, String>> listener) {
        if (!running.get()) {
            consumer = new KafkaConsumer<>(properties);
        }
        Set<String> subscription = new HashSet<>(subscription());
        boolean wasEmpty = subscription.isEmpty();
        subscription.add(topic);
        subscribe(subscription);
        listeners.put(topic, listener);
        if (wasEmpty) {
            startConsumer();
        }
    }

    public void unsubscribe(String topic) {
        Set<String> subscription = new HashSet<>(subscription());
        subscription.remove(topic);
        if (subscription.isEmpty()) {
            stopConsumer();
        } else {
            subscribe(subscription);
        }
        listeners.remove(topic);
    }

    private void startConsumer() {
        if (!running.get()) {
            if (worker == null || !worker.isAlive()) {
                worker = new Thread(this::poll, THREAD_NAME);
                worker.setDaemon(true);
                worker.start();
            }
            running.set(true);
        }
    }

    private void stopConsumer() {
        running.set(false);
        consumer.wakeup();
    }

    private void poll() {
        try {
            while (running.get()) {
                ConsumerRecords<String, String> records = poll(Duration.ofMillis(100));
                listeners.forEach((topic, listener) -> records.records(topic).forEach(listener));
            }
        } catch (WakeupException e) {
            if (running.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }

    private synchronized ConsumerRecords<String, String> poll(Duration timeout) {
        return consumer.poll(timeout);
    }

    private synchronized Set<String> subscription() {
        return consumer.subscription();
    }

    private synchronized void subscribe(Set<String> subscription) {
        consumer.subscribe(subscription);
    }

    @Override
    public void close() {
        admin.close();
        producer.close();
        stopConsumer();
    }
}

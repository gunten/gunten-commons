package org.tp.mix.kafka.partition;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.time.*;

public class TestConsumer {
    public static void main(String args[]) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "192.168.33.10:9092");
        props.setProperty("group.id", "test-group");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.offset.reset", "earliest");
        props.setProperty("auto.commit.interval.ms", "1000");

        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("test"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.partition() + ":" + record.offset());
            }
        }
    }
}

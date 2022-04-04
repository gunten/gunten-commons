package org.tp.mix.kafka.partition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TestProducer {
    public static void main(String args[]) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.16.1:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //org.apache.kafka.clients.producer.internals.DefaultPartitioner
        props.put("partitioner.class", "org.tp.mix.kafka.partition.MyPartitioner");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "org.tp.mix.kafka.partition.MyPartitioner");

        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<String, String>("test",
                    "" + i,
                    "message" + i));

            //Thread.sleep(1000L);
        }

        producer.close();
        System.out.println("done...");
    }
}

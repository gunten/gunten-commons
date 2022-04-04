package org.tp.rocketmq.producer;

import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * 2019/6/25
 */
public class ScheduledMessageProducerDemo {
    /**
    // org/apache/rocketmq/store/config/MessageStoreConfig.java
    private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
    */
    public static void main(String[] args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.setSendMsgTimeout(30000);
        // Launch producer
        producer.start();
        int totalMessagesToSend = 5;
        for (int i = 0; i < totalMessagesToSend; i++) {
            String content = "Hello scheduled message " + i + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date());
            Message message = new Message("TopicTest", content.getBytes(RemotingHelper.DEFAULT_CHARSET));

            // This message will be delivered to consumer 10 seconds later.
            // 可以在broker服务器端自行配置messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            message.setDelayTimeLevel(3);
            // Send the message
            SendResult result = producer.send(message);

            System.out.printf("发送结果：%s%n", result);
            TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(300, 800));
        }

        // Shutdown producer after use.
        producer.shutdown();
    }
}

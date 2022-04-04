package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/6/25
 */
public class BatchProducerDemo {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup-Test");
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.start();

        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String content = "Hello BatchMQ " + i;
            Message message = new Message("TopicTest", "TagA", "OrderID" + i, content.getBytes(RemotingHelper.DEFAULT_CHARSET));

            messages.add(message);
        }

        try {
            SendResult result = producer.send(messages);
            System.out.println("消息已发送：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            producer.shutdown();
        }

    }
}
package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/6/25
 */
public class FilterProducerDemo {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.setSendMsgTimeout(30000);
        producer.start();

        for (int i = 0; i < 5; i++){
            try {
                Message msg = new Message("TopicTest","TagA","OrderID188",("Hello world"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                // Set some properties.
                msg.putUserProperty("a", String.valueOf(i));

                SendResult sendResult = producer.send(msg);
                System.out.println(sendResult.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }
}
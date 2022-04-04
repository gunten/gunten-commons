package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * 2019/6/24
 */
public class OrderedProducerDemo {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.setSendMsgTimeout(30000);
        //Launch the instance.
        producer.start();

        /** 这里是队列选择器 ，还有一种MessageSelector  消息选择器*/
        MessageQueueSelector messageQueueSelector = new MessageQueueSelector() {

            /**
             * 消息队列选择器，保证同一条业务数据的消息在同一个队列
             * @param mqs topic中所有队列的集合
             * @param msg 发送的消息
             * @param arg 此参数是本示例中producer.send的第三个参数
             * @return
             */
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                // 分区顺序：同一个模值的消息在同一个队列中
                return mqs.get(index);

                // 全局顺序：所有的消息都在同一个队列中
                // return mqs.get(mqs.size() - 1);
            }
        };

        String[] tags = new String[] {"TagA", "TagB", "TagC"};
        for (int i = 0; i < 10; i++) {
            int orderId = i % 10;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest", tags[i % tags.length], "1001" ,
                    ("Hello OrderedMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));


            SendResult sendResult = producer.send(msg, messageQueueSelector, 1001);//业务号 比如1001

            System.out.printf("%s%n", sendResult);
        }
        //server shutdown
        producer.shutdown();
    }
}

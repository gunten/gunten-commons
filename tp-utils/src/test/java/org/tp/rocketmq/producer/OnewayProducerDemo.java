package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * 单向模式
 * 一般用来对可靠性有一定要求的消息发送，例如日志系统
 * 不同于同步的唯一之处在于：调用的是sendOneway方法，且方法不返回任何值，即调用者不需要关心成功或失败
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/6/24
 */
public class OnewayProducerDemo {

    public static void main(String[] args) throws Exception{
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup-Test");
        // Specify name server addresses.
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello OneWay :" +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);

        }
        System.out.println("Oneway消息已发送");
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
}

package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * 异步消息
 * 一般用来对方法调用响应时间有较严格要求的情况下，异步调用，立即返回
 * 不同于同步的唯一在于： send方法调用的时候多携带一个回调接口参数，用来异步处理消息发送结果
 * @author gunten
 * 2019/6/6
 */
public class AsyncProducerDemo {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup-Test");
        // Specify name server addresses.
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.setSendMsgTimeout(30000);
        //Launch the instance.
        producer.start();
        // 设置异步发送失败重试次数，默认为2
        producer.setRetryTimesWhenSendAsyncFailed(0);


        int count = 10;
        CountDownLatch cd = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            final int index = i;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest",
                    "TagB",
                    "ID188",
                    ("Hello World " + index).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg, new SendCallback() {
                /**
                 * 发送成功的回调函数
                 * 但会结果有多种状态，在SendStatus枚举中定义
                 * @param sendResult
                 */
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK MSG_ID:%s %n", index, sendResult.getMsgId());
                    cd.countDown();
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                    cd.countDown();
                }
            });
        }
        // 确保消息都发送出去了
        cd.await();
        //关掉就会报 No route info of this topic, xxx
        producer.shutdown();
    }
}

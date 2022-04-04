package org.tp.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * @author gunten
 * 2019/5/28
 */
public class MQConsumerDemo {


    public static void main(String[] args) throws InterruptedException, MQClientException {
        // 1. 创建消费者（Push）对象
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroup-Test");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 2. 设置NameServer的地址，如果设置了环境变量NAMESRV_ADDR，可以省略此步
        consumer.setNamesrvAddr(NAME_SERVER_ADDR);
        consumer.setMaxReconsumeTimes(-1);// 消费重试次数 -1代表16次
        // 3. 订阅对应的主题和Tag
        consumer.subscribe("TopicTest", "*");
        // 4. 注册消息接收到Broker消息后的处理接口
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for(MessageExt me:msgs){
                    try {
                        System.out.printf("线程：%-25s %-25s 收到新消息 from %s --- [%s] %n",
                                Thread.currentThread().getName(),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()),
                                me.getTags(),
                                new String(me.getBody(), RemotingHelper.DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();

        System.out.println("Consumer Started.");
    }

}
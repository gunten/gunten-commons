package org.tp.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * 批量消息消费者
 */
public class BatchMessageConsumer {

    public static void main(String[] args) throws MQClientException {

        // 1. 创建消费者（Push）对象
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GROUP_TEST");

        // 2. 设置NameServer的地址，如果设置了环境变量NAMESRV_ADDR，可以省略此步
        consumer.setNamesrvAddr(NAME_SERVER_ADDR);

        // 3. 订阅对应的主题和Tag
        consumer.subscribe("TopicTest", "*");

        // 4. 设置消息批处理数量，即每次最多获取多少消息，默认是1。
        // 默认最大32，超出无效
        consumer.setConsumeMessageBatchMaxSize(100);

        // 5. 注册接收到Broker消息后的处理接口
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                System.out.printf("线程：%-25s 拉取的消息数：%s %n", Thread.currentThread().getName(), list.size());
                try {
                    // 设置消息批处理数量后，list中才会有多条，否则每次只会有一条
                    for (MessageExt messageExt : list) {
                        System.out.printf("线程：%-25s 接收到新消息 --- %s %n", Thread.currentThread().getName(), new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 6. 启动消费者(必须在注册完消息监听器后启动，否则会报错)
        consumer.start();

        System.out.println("已启动消费者");
    }
}

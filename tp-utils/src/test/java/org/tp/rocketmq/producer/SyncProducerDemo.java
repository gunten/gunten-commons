package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 * 发送同步消息
 * 可靠的同步传输用于广泛的场景，如重要的通知消息，短信通知，短信营销系统等。
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/5/24
 */
public class SyncProducerDemo {

    public static void main(String[] args) throws Exception {
        // 1. 创建生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup-Test");
        // 2. 设置NameServer的地址，如果设置了环境变量NAMESRV_ADDR，可以省略此步
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        // 3. 启动生产者
        producer.start();
        // 4. 生产者发送消息
        for (int i = 0; i < 5; i++){
            try {
                //Message(String topic, String tags, String keys, byte[] body)
                Message msg = new Message("TopicTest", "TagB", "", ("Hello MQ:" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.send(msg);
                System.out.printf("发送结果: %s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 5. 停止生产者
        producer.shutdown();
    }
}
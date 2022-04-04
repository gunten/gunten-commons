package org.tp.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

import static org.tp.rocketmq.MQConstant.NAME_SERVER_ADDR;

/**
 *
 * 2019/6/25
 */
public class TransactionProducerDemo {

    private final static TransactionListener transactionListenerImpl = new  TransactionListener() {

//        private AtomicInteger transactionIndex = new AtomicInteger(0);

        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

        /**
         * 在发送消息成功时执行本地事务
         * @param msg
         * @param arg producer.sendMessageInTransaction的第二个参数
         * @return 返回事务状态
         * LocalTransactionState.COMMIT_MESSAGE：提交事务，提交后broker才允许消费者使用
         * LocalTransactionState.RollbackTransaction：回滚事务，回滚后消息将被删除，并且不允许别消费
         * LocalTransactionState.Unknown：中间状态，表示MQ需要核对，以确定状态
         */
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//            int value = transactionIndex.getAndIncrement();
            int value = (int) arg;
            int status = value % 3;
            localTrans.put(msg.getTransactionId(), value);

            switch (status) {
                case 0:
                    System.out.printf("事务提交，状态未知 id:%s%n", msg.getKeys());
                    return LocalTransactionState.UNKNOW;
                case 2:
                    System.out.printf("本地事务回滚，回滚消息，id:%s%n", msg.getKeys());
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                default:
                    System.out.printf("事务提交，消息正常处理 id:%s%n", msg.getKeys());
                    return LocalTransactionState.COMMIT_MESSAGE;
            }
        }


        /**
         * Broker端对未确定状态的消息发起回查，将消息发送到对应的Producer端（同一个Group的Producer），
         * 由Producer根据消息来检查本地事务的状态，进而执行Commit或者Rollback
         * @param msg
         * @return 返回事务状态
         */
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTrans.get(msg.getTransactionId());
            System.out.printf("回查事务状态 key:%-5s msgId:%-10s transactionId:%-10s %n", msg.getKeys(), msg.getMsgId(), msg.getTransactionId());
            if (null != status && status % 2 == 0) {
                System.out.printf("回查到本地事务已提交，提交消息，id:%s%n", msg.getKeys());
                return LocalTransactionState.COMMIT_MESSAGE;
            }else {
                System.out.printf("未查到本地事务状态，回滚消息，id:%s%n", msg.getKeys());
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
    };


    public static void main(String[] args) throws MQClientException, InterruptedException, IOException {
        TransactionMQProducer producer = new TransactionMQProducer("ProducerGroup-TRX");
//        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread thread = new Thread(r);
//                thread.setName("client-transaction-msg-check-thread");
//                return thread;
//            }
//        });
//
//        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListenerImpl);
        producer.setNamesrvAddr(NAME_SERVER_ADDR);
        producer.setSendMsgTimeout(6000);
        producer.start();

        for (int i = 0; i < 10; i++) {
            try {
                Message msg =
                        new Message("TopicTest", "TagA", "KEY" + i,
                                ("Hello TRX-MQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(msg, i);
                System.out.printf("发送结果：%s%n", sendResult);

            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        System.in.read();
        producer.shutdown();
    }
}

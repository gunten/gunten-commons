package org.tp.zk.serial;

import com.google.common.base.Splitter;
import com.google.common.collect.Queues;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.tp.zk.ZKPathUtil;
import org.tp.zk.exception.ZKFormatException;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统一流水号
 *
 *
 * 使用例子：
 * final Serial serial = new DistributedSerial("127.0.0.1:2181",3000);
 * serial.getSerialNum("/credit/limit/number/");
 *
 */
public class DistributedSerial implements Serial {

    protected ZkClient zkClient;

    private static final String SLASH_SEPARATOR="/";

    private LinkedBlockingQueue<String> queue = Queues.newLinkedBlockingQueue(30000);

    private IndividualNode individualNode ;



    @Override
    public String getSerialNum(String path) {
        return getSerialNum(path, individualNode);
    }

    @Override
    public String getSerialNum(String path, IndividualNode node) {
//               解决链接ZK超时时无限等待问题，由于未重写ZkClient，此处注释
//        if (!zkClient.isConnected()) {
//            zkClient.waitUntilConnected();
//        }
        List<String> strs = ZKPathUtil.analysisZkPath(path);
        String self = strs.get(strs.size() - 1); // 最后一个路径节点
        String s = "";
        s = ZKPathUtil.createZkPath(strs);
        if (!zkClient.exists(s + SLASH_SEPARATOR + self)) {
            zkClient.createPersistent(s + SLASH_SEPARATOR + self, true);
        }

        String parentPath = s + SLASH_SEPARATOR + self;
        String nodeValue = ""; // 正确的个性化节点的值
        if (node != null) {
            String n = node.createNode();
            n = (n == null) ? "" : n;
            List<String> l = Splitter.on(SLASH_SEPARATOR).omitEmptyStrings().trimResults().splitToList(n);
            if (l.size() > 1) {
                throw new ZKFormatException("个性化节点错误！");
            } else if (l.size() == 1) {
                nodeValue = l.get(0);
            }
        }

        String result = null;
        try {
            if (StringUtils.isNotEmpty(nodeValue)) {
                result = zkClient.createPersistentSequential(parentPath + SLASH_SEPARATOR + nodeValue + SLASH_SEPARATOR, null);
            } else {
                result = zkClient.createPersistentSequential(parentPath + SLASH_SEPARATOR, null);
            }
        } catch (Exception e) {
            if (!zkClient.exists(parentPath + SLASH_SEPARATOR + nodeValue)) {
                zkClient.createPersistent(parentPath + SLASH_SEPARATOR + nodeValue, true);
            }
            if (StringUtils.isNotEmpty(nodeValue)) {
                result = zkClient.createPersistentSequential(parentPath + SLASH_SEPARATOR + nodeValue + SLASH_SEPARATOR, null);
            }
            else {
                result = zkClient.createPersistentSequential(parentPath + SLASH_SEPARATOR, null);
            }
        }

        try {
            if (StringUtils.isNotEmpty(result)) {
                queue.put(result);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> list = Splitter.on(SLASH_SEPARATOR).omitEmptyStrings().trimResults().splitToList(result);
        return nodeValue + list.get(list.size() - 1);
    }

    private Runnable executeDeleteThread(final String path) {
        return new Runnable() {
            @Override
            public void run() {
//               解决链接ZK超时时无限等待问题，由于未重写ZkClient，此处注释
//                if (!zkClient.isConnected()) {
//                    zkClient.waitUntilConnected();
//                }

                zkClient.deleteRecursive(path);// 删除此路径下子节点，并删除自己

            }
        };
    }




    public DistributedSerial(String zkServers, int zkClientTimeout) {
        this(zkServers, zkClientTimeout, null);
    }

    public DistributedSerial(String zkServers, int zkClientTimeout, IndividualNode node) {
        zkClient = new ZkClient(zkServers, zkClientTimeout);
        singleThreadScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                while (!queue.isEmpty()) {
                    final String path = queue.poll();
                    executorService.submit(executeDeleteThread(path));
                }
            }
        }, 1, 2, TimeUnit.SECONDS);
        this.individualNode = node;
    }

    ExecutorService executorService = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("delete-used-serial-thread-" + threadNumber.getAndIncrement() );
            return thread;
        }
    });


    ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("select-queue-" + threadNumber.getAndIncrement());
            return thread;
        }
    });

}

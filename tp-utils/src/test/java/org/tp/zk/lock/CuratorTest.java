package org.tp.zk.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Curator 工具测试
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/3/19
 */
public class CuratorTest {

    private String zkServers = "47.98.101.251:2181,47.98.101.251:2182,47.98.101.251:2183";
    //测试用 默认值扩大10倍
    private int sessionTimeout = 60 * 1000 *10;
    private int connectionTimeout = 15 * 1000 * 10;
    private CuratorFramework client;

    @Before
    public void init() throws InterruptedException {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        client = CuratorFrameworkFactory
                .builder()
                .connectString(zkServers)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

    }

    @Test
    public void integrativeTest() throws Exception {
        String path = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/jike/1", "123".getBytes());
        System.out.println("创建节点:" + path);

        String path2 = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/jike/20", "20*20".getBytes());
        System.out.println("创建节点:" + path2);


        client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath("/jike/20");

        //获取节点
        List<String> cList = client.getChildren().forPath("/jike");
        System.out.println(cList.toString());

        //获取节点内容
        Stat stat = new Stat();
        byte[] ret = client.getData().storingStatIn(stat).forPath("/jike");
        System.out.println(new String(ret));
        System.out.println(stat);

        //节点修改
        client.setData().withVersion(stat.getVersion()).forPath("/jike", "123".getBytes());

        ExecutorService es = Executors.newFixedThreadPool(5);

        client.checkExists().inBackground(new BackgroundCallback() {

            public void processResult(CuratorFramework arg0, CuratorEvent arg1)
                    throws Exception {

                Stat stat = arg1.getStat();
                System.out.println(stat);
                System.out.println(arg1.getContext());

            }
        },"123",es).forPath("/jike");

        InterProcessMutex lock = new InterProcessMutex(client, "/test");
        try {
            lock.acquire();
            System.out.println("获得锁");
            //业务
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
            System.out.println("释放锁");
        }

    }


}

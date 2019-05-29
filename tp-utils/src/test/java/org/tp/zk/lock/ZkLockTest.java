package org.tp.zk.lock;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ZkLockTest {

    private static String hosts = "127.0.0.1:2181";
    int count = 10;
    CountDownLatch latch;
    ExecutorService executor;
    DistributedLock distributedLock ;

    @Before
    public void init() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("Orders-%d")
                .build();
        executor = Executors.newCachedThreadPool(namedThreadFactory);
        latch = new CountDownLatch(count);
        distributedLock = new DistributedLock(hosts,10000);
    }


    @Test
    public  void zkLockBlockTest() {

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    distributedLock.tryLock("/locknode"); // 阻塞获取锁
                    System.out.println("----------------" + Thread.currentThread().getName() + " locked");
                    //让每个线程谁1秒，让更多的线程去尝试竞争锁资源
                    Thread.sleep(1000);
                    distributedLock.unLock();
                    System.out.println("----------------" + Thread.currentThread().getName() + " released lock");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }

            });
        }
        try {
            latch.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public  void zkLockWithWaitTimeTest() {

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    if (distributedLock.tryLock("/locknode", 5000)) {//带超时获取锁
                        System.out.println("----------------" + Thread.currentThread().getName() + " locked "+ distributedLock.getNode());
                        //让每个线程谁1秒，让更多的线程去尝试竞争锁资源
                        Thread.sleep(100);
                        distributedLock.unLock();
                        System.out.println("----------------"+Thread.currentThread().getName() + " released "+ distributedLock.getNode());
                    }else {
                        System.out.println("----------------" + Thread.currentThread().getName() + " locked failed "+ distributedLock.getNode());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     *  非正常使用测试，加锁失败也去人工解锁
     */
    @Test
    public  void zkLockErrorTest() {

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    boolean ret = distributedLock.tryLock("/locknode", 5000);//带超时获取锁
                    System.out.println("----------------" + Thread.currentThread().getName() + " lock " +ret + ","+distributedLock.getNode());
                    //让每个线程谁1秒，让更多的线程去尝试竞争锁资源
                    Thread.sleep(100);
                    distributedLock.unLock();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

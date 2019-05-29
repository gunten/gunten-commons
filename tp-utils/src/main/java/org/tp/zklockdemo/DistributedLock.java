package org.tp.zklockdemo;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/3/19
 */
public interface DistributedLock {

    /*
     * 获取锁，如果没有得到就等待
     */
    void acquire() throws Exception;

    /*
     * 获取锁，直到超时
     */
    boolean acquire(long time, TimeUnit unit) throws Exception;

    /*
     * 释放锁
     */
    void release() throws Exception;
}

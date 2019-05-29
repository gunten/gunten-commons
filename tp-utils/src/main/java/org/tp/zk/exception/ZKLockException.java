package org.tp.zk.exception;

/**
 * 锁异常
 */
public class ZKLockException extends RuntimeException {

    public ZKLockException(String msg) {
        super(msg);
    }


    public ZKLockException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

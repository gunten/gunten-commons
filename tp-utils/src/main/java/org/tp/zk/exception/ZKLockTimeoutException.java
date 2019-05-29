package org.tp.zk.exception;

/**
 * 锁超时异常
 */
public class ZKLockTimeoutException extends RuntimeException{
    public ZKLockTimeoutException(String msg) {
        super(msg);
    }
}

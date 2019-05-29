package org.tp.zk.exception;

/**
 * 格式不对
 */
public class ZKFormatException extends RuntimeException {
    public ZKFormatException(String msg) {
        super(msg);
    }
}

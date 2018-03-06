package com.watent.soa.exception;

/**
 * 自定义异常
 *
 * @author Atom
 */
public class SOAException extends RuntimeException {

    public SOAException() {
        super();
    }

    public SOAException(String message) {
        super(message);
    }

    public SOAException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOAException(Throwable cause) {
        super(cause);
    }

    protected SOAException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

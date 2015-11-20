package com.heaven7.databinding.core;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class DataBindException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataBindException() {
    }
    public DataBindException(String detailMessage) {
        super(detailMessage);
    }
    public DataBindException(Throwable throwable) {
        super(throwable);
    }

    public DataBindException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

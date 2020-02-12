package com.naaz.ioms.app.services.exception;

import java.io.Serializable;

public class IomsDbAccessException extends Throwable implements Serializable {
    private static final long serialVersionUID = 21L;

    private int code;
    private final String message;


    public IomsDbAccessException(String message) {
        this.message = message;
    }

    public IomsDbAccessException(int status, String message) {
        this.message = message;
        this.code = status;
    }

    public int getStatus() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

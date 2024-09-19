package com.carbonhater.co2zerobookmark.common.exception;

public class IllegalParameterException extends CustomRuntimeException {
    public IllegalParameterException(String message, Object... args) {
        super(message, args);
    }
}

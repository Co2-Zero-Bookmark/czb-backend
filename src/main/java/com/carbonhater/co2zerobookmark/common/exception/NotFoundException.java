package com.carbonhater.co2zerobookmark.common.exception;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(String message, Object... args) {
        super(message, args);
    }
}

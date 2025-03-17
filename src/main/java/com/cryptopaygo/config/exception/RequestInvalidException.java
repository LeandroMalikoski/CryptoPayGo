package com.cryptopaygo.config.exception;

public class RequestInvalidException extends RuntimeException {
    public RequestInvalidException(String message) {
        super(message);
    }
}

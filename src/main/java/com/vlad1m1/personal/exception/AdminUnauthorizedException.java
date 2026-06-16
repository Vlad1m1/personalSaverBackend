package com.vlad1m1.personal.exception;

public class AdminUnauthorizedException extends RuntimeException {
    public AdminUnauthorizedException() {
        super("Missing or invalid admin API key");
    }
}

package com.vlad1m1.personal.exception;

import java.util.Map;

public class ApiValidationException extends RuntimeException {
    private final Map<String, String> details;

    public ApiValidationException(String message, Map<String, String> details) {
        super(message);
        this.details = details;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}

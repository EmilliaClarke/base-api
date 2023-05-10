package com.akami.core.exception;

public class ExceptionError extends Error {
    private String errorMessage;
    private Throwable errorCause;

    public ExceptionError(String message, Throwable cause) {
        super(message, cause);
        errorMessage = message;
        errorCause = cause;
    }

    public ExceptionError(String message) {
        super(message);
        errorMessage = message;
    }

    @Override
    public String getMessage() {
        if (errorCause != null) {
            return errorMessage + errorCause;
        }
        return errorMessage;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}

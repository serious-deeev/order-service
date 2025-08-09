package org.serious.dev.exception;

public abstract class GrpcErrorTypedClientException extends RuntimeException {

    protected GrpcErrorTypedClientException(String message) {
        super(message);
    }

    protected GrpcErrorTypedClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract RemoteErrorCode getErrorType();
}

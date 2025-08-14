package org.serious.dev.exception;

import io.grpc.StatusRuntimeException;

public class ExternalGrpcServiceException extends RuntimeException {

    public ExternalGrpcServiceException(StatusRuntimeException e) {
        super(e);
    }
}

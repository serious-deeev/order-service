package org.serious.dev.web.advice;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.serious.dev.enums.RemoteErrorCode;
import org.serious.dev.exception.ExternalGrpcServiceException;
import org.serious.dev.logging.HttpLogger;
import org.serious.dev.logging.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Optional;

import static com.google.rpc.Status.getDefaultInstance;
import static io.grpc.protobuf.ProtoUtils.keyForProto;
import static java.util.Map.of;
import static org.serious.dev.logging.util.RequestContextUtil.getRequestId;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class OrderGlobalExceptionHandler {

    private final HttpLogger httpLogger;
    private final ServiceLogger serviceLogger;

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleStatusRuntimeException(StatusRuntimeException e) {
        com.google.rpc.Status status = getStatus(e);
        if (status != null) {
            String errorDescription = e.getStatus().getDescription();
            serviceLogger.logGrpcError(getRequestId(), errorDescription);
            HttpStatus httpStatus = mapGrpcErrorCodeToHttpStatus(status);
            return buildResponse(errorDescription, httpStatus);
        }

        return handleException(new ExternalGrpcServiceException(e));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        String errorMessage = e.getMessage();
        httpLogger.logError(getRequestId(), errorMessage, e);

        return buildResponse(errorMessage, INTERNAL_SERVER_ERROR);
    }

    private com.google.rpc.Status getStatus(StatusRuntimeException e) {
        return Optional.ofNullable(e.getTrailers())
                .map(metadata -> metadata.get(keyForProto(getDefaultInstance())))
                .orElse(null);
    }

    private RemoteErrorCode extractRemoteErrorCode(com.google.rpc.Status status) {
        return RemoteErrorCode.fromCode(status.getMessage());
    }


    private ResponseEntity<Map<String, String>> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(of("Что-то пошло не так", message), status);
    }

    private HttpStatus mapGrpcErrorCodeToHttpStatus(com.google.rpc.Status status) {
        RemoteErrorCode errorCode = extractRemoteErrorCode(status);
        return switch (errorCode) {
            case USER_NOT_FOUND, POST_NOT_FOUND -> NOT_FOUND;
            case POST_ALREADY_RESERVED -> CONFLICT;
            case POST_RESERVATION_ERROR, POST_CANCEL_ERROR -> INTERNAL_SERVER_ERROR;
        };
    }
}

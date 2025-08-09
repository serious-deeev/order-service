package org.serious.dev.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.serious.dev.logging.ServiceLogger;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static java.util.Map.of;
import static org.serious.dev.web.filter.RequestIdConstants.REQUEST_ID_KEY;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class OrderGlobalExceptionHandler {

    private final ServiceLogger serviceLogger;

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleGrpcClientException(GrpcErrorTypedClientException e) {
        String errorMessage = e.getMessage();
        serviceLogger.logGrpcError(MDC.get(REQUEST_ID_KEY), errorMessage);

        return buildResponse(errorMessage, mapGrpcErrorCodeToHttpStatus(e.getErrorType()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        String errorMessage = e.getMessage();
        serviceLogger.logError(MDC.get(REQUEST_ID_KEY), errorMessage, e);

        return buildResponse(errorMessage, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(of("Что-то пошло не так", message), status);
    }

    private HttpStatus mapGrpcErrorCodeToHttpStatus(RemoteErrorCode code) {
        return switch (code) {
            case USER_NOT_FOUND, POST_NOT_FOUND -> NOT_FOUND;
            case POST_ALREADY_RESERVED -> CONFLICT;
            case POST_RESERVATION_ERROR, POST_CANCEL_ERROR -> INTERNAL_SERVER_ERROR;
        };
    }
}

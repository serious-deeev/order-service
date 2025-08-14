package org.serious.dev.logging;

import lombok.RequiredArgsConstructor;
import org.serious.dev.dto.OrderRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoggingAdapter {

    private final HttpLogger httpLogger;
    private final ServiceLogger serviceLogger;

    public void logRequest(String requestId, Object request) {
        if (request instanceof OrderRequestDto orderRequestDto) {
            httpLogger.logHttpRequest(requestId, orderRequestDto);
        } else {
            serviceLogger.logGrpcRequest(requestId, request);
        }
    }

    public void logResponse(String requestId, Object response) {
        if (response instanceof ResponseEntity<?>) {
            httpLogger.logHttpResponse(requestId, response);
        } else {
            serviceLogger.logGrpcResponse(requestId, response);
        }
    }
}

package org.serious.dev.logging;

import lombok.extern.slf4j.Slf4j;
import org.serious.dev.dto.OrderRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpLogger {

    public void logHttpRequest(String requestId, OrderRequestDto requestDto) {
        log.info(
                "[{}] поступил запрос на бронирование поста с id={} пользователя userId={}",
                requestId,
                requestDto.postId(),
                requestDto.userId()
        );
    }

    public void logHttpResponse(String requestId, Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            log.info(
                    "[{}] процесс бронирования поста успешно завершен с кодом {}",
                    requestId,
                    responseEntity.getStatusCode()
            );
        } else {
            log.warn(
                    "[{}] получен неожиданный тип ответа для логирования: {}",
                    requestId,
                    result.getClass().getSimpleName());
        }
    }

    public void logError(String requestId, String errorMessage, Exception e) {
        log.error(
                "[{}] в процессе обработки запроса возникла ошибка: {}",
                requestId,
                errorMessage,
                e
        );
    }
}

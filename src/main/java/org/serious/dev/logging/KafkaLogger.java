package org.serious.dev.logging;

import lombok.extern.slf4j.Slf4j;
import org.serious.dev.kafka.event.OrderEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaLogger {

    public void logEventSendAttempt(String requestId, OrderEvent orderEvent, String orderTopic, String eventKey) {
        log.info(
                "[{}] попытка отправки сообщения в kafka: topic={}, key={}, type={}, payload={}",
                requestId,
                orderTopic,
                eventKey,
                orderEvent.getClass().getSimpleName(),
                orderEvent
        );
    }

    public void logEventSendSuccess(String requestId, String orderTopic, String eventKey) {
        log.info(
                "[{}] сообщение успешно отправлено в kafka: topic={}, key={}",
                requestId,
                orderTopic,
                eventKey
        );
    }

    public void logEventSendError(String requestId, Throwable e) {
        log.error(
                "[{}] ошибка при попытке отправить сообщение в kafka:",
                requestId,
                e
        );
    }
}

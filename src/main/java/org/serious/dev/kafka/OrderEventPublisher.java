package org.serious.dev.kafka;

import lombok.RequiredArgsConstructor;
import org.serious.dev.kafka.config.KafkaTopicConfig;
import org.serious.dev.kafka.event.OrderCreatedEvent;
import org.serious.dev.kafka.event.OrderEvent;
import org.serious.dev.kafka.event.OrderFailedEvent;
import org.serious.dev.logging.KafkaLogger;
import org.serious.dev.mapper.OrderEventMapper;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.serious.dev.web.filter.RequestIdConstants.REQUEST_ID_KEY;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTopicConfig kafkaTopicConfig;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final OrderEventMapper orderEventMapper;
    private final KafkaLogger kafkaLogger;

    public void sendOrderCreatedEvent(Long orderId, Long userId, Long postId) {
        OrderCreatedEvent orderCreatedEvent = orderEventMapper.toOrderCreatedEvent(orderId, userId, postId);
        sendEventToKafka(postId, orderCreatedEvent);
    }

    public void sendOrderFailedEvent(Long userId, Long postId) {
        OrderFailedEvent orderFailedEvent = orderEventMapper.toOrderFailedEvent(userId, postId);
        sendEventToKafka(postId, orderFailedEvent);
    }

    private void sendEventToKafka(Long postId, OrderEvent orderEvent) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        String orderTopic = kafkaTopicConfig.getOrderTopic();
        String eventKey = String.valueOf(postId);

        kafkaLogger.logEventSendAttempt(requestId, orderEvent, orderTopic, eventKey);
        kafkaTemplate.send(orderTopic, eventKey, orderEvent)
                .whenComplete((sendResult, exception) -> {
                    if (exception == null) {
                        kafkaLogger.logEventSendSuccess(requestId, orderTopic, eventKey);
                    } else {
                        kafkaLogger.logEventSendError(requestId, exception);
                    }
                });
    }
}

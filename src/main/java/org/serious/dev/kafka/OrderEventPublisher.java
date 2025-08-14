package org.serious.dev.kafka;

import lombok.RequiredArgsConstructor;
import org.serious.dev.kafka.config.KafkaTopicConfig;
import org.serious.dev.kafka.event.KafkaEventSender;
import org.serious.dev.kafka.event.OrderCreatedEvent;
import org.serious.dev.kafka.event.OrderEvent;
import org.serious.dev.kafka.event.OrderFailedEvent;
import org.serious.dev.mapper.OrderEventMapper;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;
import static org.serious.dev.logging.util.RequestContextUtil.getRequestId;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTopicConfig kafkaTopicConfig;
    private final KafkaEventSender kafkaEventSender;
    private final OrderEventMapper orderEventMapper;

    public void sendOrderCreatedEvent(Long orderId, Long userId, Long postId) {
        OrderCreatedEvent orderCreatedEvent = orderEventMapper.toOrderCreatedEvent(getRequestId(), orderId, userId, postId);
        sendEventToKafka(postId, orderCreatedEvent);
    }

    public void sendOrderFailedEvent(Long userId, Long postId) {
        OrderFailedEvent orderFailedEvent = orderEventMapper.toOrderFailedEvent(getRequestId(), userId, postId);
        sendEventToKafka(postId, orderFailedEvent);
    }

    private void sendEventToKafka(Long eventKey, OrderEvent orderEvent) {
        String orderTopic = kafkaTopicConfig.getOrderTopic();
        kafkaEventSender.send(orderTopic, valueOf(eventKey), orderEvent);
    }
}

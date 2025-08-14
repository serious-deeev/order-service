package org.serious.dev.mapper;


import org.serious.dev.kafka.event.OrderCreatedEvent;
import org.serious.dev.kafka.event.OrderFailedEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderEventMapper {

    public OrderCreatedEvent toOrderCreatedEvent(String requestId, Long orderId, Long userId, Long postId) {
        return OrderCreatedEvent.builder()
                .requestId(requestId)
                .orderId(orderId)
                .userId(userId)
                .postId(postId)
                .build();
    }

    public OrderFailedEvent toOrderFailedEvent(String requestId, Long userId, Long postId) {
        return OrderFailedEvent.builder()
                .requestId(requestId)
                .userId(userId)
                .postId(postId)
                .build();
    }
}

package org.serious.dev.mapper;


import org.serious.dev.kafka.event.OrderCreatedEvent;
import org.serious.dev.kafka.event.OrderFailedEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderEventMapper {

    public OrderCreatedEvent toOrderCreatedEvent(Long orderId, Long userId, Long postId) {
        return OrderCreatedEvent.builder()
                .orderId(orderId)
                .userId(userId)
                .postId(postId)
                .build();
    }

    public OrderFailedEvent toOrderFailedEvent(Long userId, Long postId) {
        return OrderFailedEvent.builder()
                .userId(userId)
                .postId(postId)
                .build();
    }
}

package org.serious.dev.mapper;

import org.serious.dev.entity.Order;
import org.serious.dev.entity.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrderEntity(Long userId, Long postId, OrderStatus status) {
        return Order.builder()
                .userId(userId)
                .postId(postId)
                .status(status)
                .build();
    }
}

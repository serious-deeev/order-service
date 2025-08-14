package org.serious.dev.service.impl;

import lombok.RequiredArgsConstructor;
import org.serious.dev.entity.Order;
import org.serious.dev.exception.OrderCreationException;
import org.serious.dev.mapper.OrderMapper;
import org.serious.dev.repository.OrderRepository;
import org.serious.dev.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.serious.dev.enums.OrderStatus.CREATED;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Long createOrder(Long userId, Long postId) {
        try {
            Order order = orderRepository.save(orderMapper.toOrderEntity(userId, postId, CREATED));
            return order.getId();
        } catch (Exception e) {
            throw new OrderCreationException(userId, postId, e);
        }
    }
}

package org.serious.dev.service;

import lombok.RequiredArgsConstructor;
import org.serious.dev.dto.OrderRequestDto;
import org.serious.dev.dto.OrderResponseDto;
import org.serious.dev.grpc.adapter.OrderGrpcAdapter;
import org.serious.dev.kafka.OrderEventPublisher;
import org.serious.dev.mapper.OrderResponseMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final OrderGrpcAdapter orderGrpcAdapter;
    private final OrderService orderService;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderResponseMapper orderResponseMapper;

    public OrderResponseDto processOrderCreation(OrderRequestDto orderRequest) {
        Long userId = orderRequest.userId();
        Long postId = orderRequest.postId();

        orderGrpcAdapter.findAndReservePost(userId, postId);
        Long orderId = orderService.createOrder(userId, postId);
        orderEventPublisher.sendOrderCreatedEvent(orderId, userId, postId);

        return orderResponseMapper.toOrderResponseDto(orderId);
    }
}

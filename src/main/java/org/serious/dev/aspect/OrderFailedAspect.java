package org.serious.dev.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.serious.dev.dto.OrderRequestDto;
import org.serious.dev.exception.OrderCreationException;
import org.serious.dev.kafka.OrderEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OrderFailedAspect {

    private final OrderEventPublisher orderEventPublisher;

    @AfterThrowing(
            pointcut = "execution(public org.serious.dev.dto.OrderResponseDto processOrderCreation(*)) && args(orderRequestDto)",
            throwing = "e"
    )
    public void orderFailed(OrderRequestDto orderRequestDto, OrderCreationException e) {
        orderEventPublisher.sendOrderFailedEvent(orderRequestDto.userId(), orderRequestDto.postId());
    }
}

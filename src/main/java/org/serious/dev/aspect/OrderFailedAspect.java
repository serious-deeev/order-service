package org.serious.dev.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.serious.dev.exception.OrderCreationException;
import org.serious.dev.kafka.OrderEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OrderFailedAspect {

    private final OrderEventPublisher orderEventPublisher;

    @AfterThrowing(
            pointcut = "execution(public * org.serious.dev.service.OrderService.createOrder(..)) && args(userId, postId)",
            throwing = "e"
    )
    public void orderFailed(Long userId, Long postId, OrderCreationException e) {
        orderEventPublisher.sendOrderFailedEvent(userId, postId);
    }
}

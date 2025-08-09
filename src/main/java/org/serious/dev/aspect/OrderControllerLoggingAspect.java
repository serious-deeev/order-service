package org.serious.dev.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.serious.dev.dto.OrderRequestDto;
import org.serious.dev.logging.OrderRequestLogger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static org.serious.dev.web.filter.RequestIdConstants.REQUEST_ID_KEY;

@Aspect
@Component
@RequiredArgsConstructor
public class OrderControllerLoggingAspect {

    private final OrderRequestLogger orderRequestLogger;

    @Around("execution(* org.serious.dev.web.controller.OrderController.takeOrder(..))")
    public Object logTakeOrderMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        OrderRequestDto requestDto = (OrderRequestDto) args[0];
        String requestId = MDC.get(REQUEST_ID_KEY);

        orderRequestLogger.logHttpRequest(requestId, requestDto);
        Object result = joinPoint.proceed();
        orderRequestLogger.logHttpResponse(requestId, result);

        return result;
    }
}

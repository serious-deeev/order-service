package org.serious.dev.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.serious.dev.kafka.event.OrderEvent;
import org.serious.dev.logging.KafkaLogger;
import org.serious.dev.logging.LoggingAdapter;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static org.serious.dev.logging.util.RequestContextUtil.getRequestId;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final LoggingAdapter loggingAdapter;
    private final KafkaLogger kafkaLogger;

    @Around(
            value =
                    "(execution(* org.serious.dev.web.controller.OrderController.takeOrder(..)) && args(request)) || " +
                    "(execution(* org.serious.dev.grpc.client.GrpcCallExecutor.execute(..)) && args(request, ..))",
            argNames = "joinPoint, request"
    )
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint, Object request) throws Throwable {
        String requestId = getRequestId();
        loggingAdapter.logRequest(requestId, request);
        Object result = joinPoint.proceed();
        loggingAdapter.logResponse(requestId, result);

        return result;
    }

    @Around(
            value = "execution(* org.serious.dev.kafka.event.KafkaEventSender.send(..)) && args(topic, key, event)",
            argNames = "joinPoint, topic, key, event"
    )
    public Object logKafkaSend(ProceedingJoinPoint joinPoint, String topic, String key, OrderEvent event) throws Throwable {
        String requestId = getRequestId();
        kafkaLogger.logEventSendAttempt(requestId, topic, event, key);

        CompletableFuture<SendResult<String, OrderEvent>> future =
                (CompletableFuture<SendResult<String, OrderEvent>>) joinPoint.proceed();

        future.whenComplete((sendResult, exception) -> {
            if (exception == null) {
                kafkaLogger.logEventSendSuccess(requestId, topic, key);
            } else {
                kafkaLogger.logEventSendError(requestId, exception);
            }
        });

        return future;
    }
}

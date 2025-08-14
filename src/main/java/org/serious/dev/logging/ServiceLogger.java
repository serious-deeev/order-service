package org.serious.dev.logging;

import lombok.extern.slf4j.Slf4j;
import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.PostResponse;
import org.serious.dev.grpc.ReservePostRequest;
import org.serious.dev.grpc.ReservePostResponse;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceLogger {

    public void logGrpcRequest(String requestId, Object grpcRequest) {
        if (grpcRequest instanceof UserRequest request) {
            log.info(
                    "[{}] выполняем grpc-запрос на поиск пользователя c userId={}",
                    requestId,
                    request.getId()
            );

        } else if (grpcRequest instanceof PostRequest request) {
            log.info(
                    "[{}] выполняем grpc-запрос на поиск поста c postId={}",
                    requestId,
                    request.getId()
            );

        } else if (grpcRequest instanceof ReservePostRequest request) {
            log.info(
                    "[{}] выполняем grpc-запрос для резервирования поста с postId={} пользователя с userId={}",
                    requestId,
                    request.getId(),
                    request.getUserId()
            );
        }
    }

    public void logGrpcResponse(String requestId, Object grpcResponse) {
        if (grpcResponse instanceof UserResponse response) {
            log.info(
                    "[{}] найден пользователь с userId = {}",
                    requestId,
                    response.getId()
            );

        } else if (grpcResponse instanceof PostResponse response) {
            log.info(
                    "[{}] найден доступный пост с postId = {}",
                    requestId,
                    response.getId()
            );

        } else if (grpcResponse instanceof ReservePostResponse) {
            log.info(
                    "[{}] пост успешно зарезервирован",
                    requestId
            );
        }
    }

    public void logGrpcError(String requestId, String errorMessage) {
        log.error(
                "[{}] в процессе обработки запроса возникла ошибка: {}",
                requestId,
                errorMessage
        );
    }
}

package org.serious.dev.grpc.adapter;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.serious.dev.exception.ExternalGrpcServiceException;
import org.serious.dev.exception.NoSuchPostException;
import org.serious.dev.exception.NoSuchUserException;
import org.serious.dev.exception.PostAlreadyReservedException;
import org.serious.dev.exception.PostCancelException;
import org.serious.dev.exception.PostReservationException;
import org.serious.dev.exception.RemoteErrorCode;
import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.PostServiceGrpc;
import org.serious.dev.grpc.ReservePostRequest;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserServiceGrpc;
import org.serious.dev.logging.ServiceLogger;
import org.serious.dev.mapper.GrpcMapper;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

import static com.google.rpc.Status.getDefaultInstance;
import static io.grpc.protobuf.ProtoUtils.keyForProto;
import static org.serious.dev.web.filter.RequestIdConstants.REQUEST_ID_KEY;

@Component
@RequiredArgsConstructor
public class OrderGrpcAdapter {

    private final UserServiceGrpc.UserServiceBlockingStub userGrpcClient;
    private final PostServiceGrpc.PostServiceBlockingStub postGrpcClient;
    private final GrpcMapper grpcMapper;
    private final ServiceLogger serviceLogger;

    public void findAndReservePost(Long userId, Long postId) {
        UserRequest grpcUserRequest = grpcMapper.toGrpcUserRequest(userId);
        PostRequest grpcPostRequest = grpcMapper.toGrpcPostRequest(postId);
        ReservePostRequest grpcReservePostRequest = grpcMapper.toGrpcReservePostRequest(postId, userId);

        doGrpcCall(userId, postId, grpcUserRequest, userGrpcClient::getUser);
        doGrpcCall(userId, postId, grpcPostRequest, postGrpcClient::getPost);
        doGrpcCall(userId, postId, grpcReservePostRequest, postGrpcClient::reservePost);
    }

    private <Req, Res> void doGrpcCall(Long userId, Long postId, Req grpcRequest, Function<Req, Res> grpcCall) {
        String requestId = MDC.get(REQUEST_ID_KEY);
        serviceLogger.logGrpcRequest(requestId, grpcRequest);
        try {
            Res grpcResponse = grpcCall.apply(grpcRequest);
            serviceLogger.logGrpcResponse(requestId, grpcResponse);
        } catch (StatusRuntimeException e) {
            throw mapToDomainException(userId, postId, e);
        }
    }

    private RuntimeException mapToDomainException(Long userId, Long postId, StatusRuntimeException e) {
        com.google.rpc.Status status = getStatus(e);
        if (status != null) {
            RemoteErrorCode errorCode = extractRemoteErrorCode(status);
            return switch (errorCode) {
                case USER_NOT_FOUND -> new NoSuchUserException(userId);
                case POST_NOT_FOUND -> new NoSuchPostException(postId);
                case POST_ALREADY_RESERVED -> new PostAlreadyReservedException(userId, postId);
                case POST_RESERVATION_ERROR -> new PostReservationException(postId, userId, e);
                case POST_CANCEL_ERROR -> new PostCancelException(postId, userId, e);
            };
        }

        return new ExternalGrpcServiceException(e);
    }

    private com.google.rpc.Status getStatus(StatusRuntimeException e) {
        return Optional.ofNullable(e.getTrailers())
                .map(metadata -> metadata.get(keyForProto(getDefaultInstance())))
                .orElse(null);
    }

    private RemoteErrorCode extractRemoteErrorCode(com.google.rpc.Status status) {
        return RemoteErrorCode.fromCode(status.getMessage());
    }
}

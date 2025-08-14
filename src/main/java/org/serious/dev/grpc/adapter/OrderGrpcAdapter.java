package org.serious.dev.grpc.adapter;

import lombok.RequiredArgsConstructor;
import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.PostServiceGrpc;
import org.serious.dev.grpc.ReservePostRequest;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserServiceGrpc;
import org.serious.dev.grpc.client.GrpcCallExecutor;
import org.serious.dev.mapper.GrpcMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderGrpcAdapter {

    private final GrpcCallExecutor grpcCallExecutor;
    private final UserServiceGrpc.UserServiceBlockingStub userGrpcClient;
    private final PostServiceGrpc.PostServiceBlockingStub postGrpcClient;
    private final GrpcMapper grpcMapper;

    public void findAndReservePost(Long userId, Long postId) {
        UserRequest grpcUserRequest = grpcMapper.toGrpcUserRequest(userId);
        PostRequest grpcPostRequest = grpcMapper.toGrpcPostRequest(postId);
        ReservePostRequest grpcReservePostRequest = grpcMapper.toGrpcReservePostRequest(postId, userId);

        grpcCallExecutor.execute(grpcUserRequest, userGrpcClient::getUser);
        grpcCallExecutor.execute(grpcPostRequest, postGrpcClient::getPost);
        grpcCallExecutor.execute(grpcReservePostRequest, postGrpcClient::reservePost);
    }
}

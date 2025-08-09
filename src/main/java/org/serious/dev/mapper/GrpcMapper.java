package org.serious.dev.mapper;

import org.serious.dev.grpc.PostRequest;
import org.serious.dev.grpc.ReservePostRequest;
import org.serious.dev.grpc.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class GrpcMapper {

    public UserRequest toGrpcUserRequest(Long userId) {
        return UserRequest.newBuilder()
                .setId(userId)
                .build();
    }

    public PostRequest toGrpcPostRequest(Long postId) {
        return PostRequest.newBuilder()
                .setId(postId)
                .build();
    }

    public ReservePostRequest toGrpcReservePostRequest(Long postId, Long userId) {
        return ReservePostRequest.newBuilder()
                .setId(postId)
                .setUserId(userId)
                .build();
    }
}

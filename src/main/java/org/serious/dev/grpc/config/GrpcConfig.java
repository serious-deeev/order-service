package org.serious.dev.grpc.config;

import org.serious.dev.grpc.PostServiceGrpc;
import org.serious.dev.grpc.UserServiceGrpc;
import org.serious.dev.grpc.client.GrpcClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public GrpcClientFactory grpcClientFactory() {
        return new GrpcClientFactory();
    }

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub grpcUserClient(
            @Value("${grpc.server.user-service.url}") String grpcServerUrl,
            @Value("${grpc.server.user-service.port}") Integer grpcServerPort
    ) {
        return grpcClientFactory().createGrpcClient(
                grpcServerUrl,
                grpcServerPort,
                channel -> UserServiceGrpc.newBlockingStub(channel)
        );
    }

    @Bean
    public PostServiceGrpc.PostServiceBlockingStub grpcPostClient(
            @Value("${grpc.server.post-service.url}") String grpcServerUrl,
            @Value("${grpc.server.post-service.port}") Integer grpcServerPort
    ) {
        return grpcClientFactory().createGrpcClient(
                grpcServerUrl,
                grpcServerPort,
                channel -> PostServiceGrpc.newBlockingStub(channel)
        );
    }
}

package org.serious.dev.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.serious.dev.grpc.interceptor.GrpcClientInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GrpcClientFactory {

    private final Map<String, ManagedChannel> channelCache = new ConcurrentHashMap<>();

    public <T> T createGrpcClient(String url, Integer port, Function<ManagedChannel, T> stubCreator) {
        String key = url + ":" + port;

        ManagedChannel channel = channelCache.computeIfAbsent(
                key,
                k -> ManagedChannelBuilder.forAddress(url, port)
                        .usePlaintext()
                        .intercept(new GrpcClientInterceptor())
                        .build()
        );

        return stubCreator.apply(channel);
    }

    @PreDestroy
    public void shutdownAllChannels() {
        channelCache.values()
                .forEach(grpcChannel -> grpcChannel.shutdown());
    }
}

package org.serious.dev.grpc.client;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GrpcCallExecutor {

    public <Req, Res> Res execute(Req grpcRequest, Function<Req, Res> grpcCall) {
        return grpcCall.apply(grpcRequest);
    }
}

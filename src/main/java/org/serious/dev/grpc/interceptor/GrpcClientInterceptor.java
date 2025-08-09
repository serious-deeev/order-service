package org.serious.dev.grpc.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import org.slf4j.MDC;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;
import static io.grpc.Metadata.Key;
import static org.serious.dev.web.filter.RequestIdConstants.REQUEST_ID_KEY;

public class GrpcClientInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> X_REQUEST_ID_HEADER =
            Key.of("x-request-id", ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next
    ) {
        ClientCall<ReqT, RespT> delegate = next.newCall(method, callOptions);
        return new ForwardingClientCall.SimpleForwardingClientCall<>(delegate) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String requestId = MDC.get(REQUEST_ID_KEY);
                if (requestId != null) headers.put(X_REQUEST_ID_HEADER, requestId);
                super.start(responseListener, headers);
            }
        };
    }
}

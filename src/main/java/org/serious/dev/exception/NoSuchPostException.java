package org.serious.dev.exception;

import static org.serious.dev.exception.RemoteErrorCode.POST_NOT_FOUND;

public class NoSuchPostException extends GrpcErrorTypedClientException {

    private static final RemoteErrorCode ERROR_TYPE = POST_NOT_FOUND;
    private static final String TEMPLATE = "пост id=%d не найден";

    public NoSuchPostException(Long postId) {
        super(String.format(TEMPLATE, postId));
    }

    @Override
    public RemoteErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}

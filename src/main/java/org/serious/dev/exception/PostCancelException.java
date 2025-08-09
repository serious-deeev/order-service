package org.serious.dev.exception;

import static org.serious.dev.exception.RemoteErrorCode.POST_CANCEL_ERROR;

public class PostCancelException extends GrpcErrorTypedClientException {

    private static final RemoteErrorCode ERROR_TYPE = POST_CANCEL_ERROR;
    private static final String TEMPLATE = "неуспешная попытка отмены резерва поста id=%d пользователя userId=%d";

    public PostCancelException(Long id, Long userId, Exception e) {
        super(String.format(TEMPLATE, id, userId), e);
    }

    @Override
    public RemoteErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}

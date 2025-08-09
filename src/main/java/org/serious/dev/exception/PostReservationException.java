package org.serious.dev.exception;

import static org.serious.dev.exception.RemoteErrorCode.POST_RESERVATION_ERROR;

public class PostReservationException extends GrpcErrorTypedClientException {

    private static final RemoteErrorCode ERROR_TYPE = POST_RESERVATION_ERROR;
    private static final String TEMPLATE = "неуспешная попытка резервирования поста id=%d пользователя userId=%d";

    public PostReservationException(Long id, Long userId, Exception e) {
        super(String.format(TEMPLATE, id, userId), e);
    }

    @Override
    public RemoteErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}

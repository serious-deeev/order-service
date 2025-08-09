package org.serious.dev.exception;

import static org.serious.dev.exception.RemoteErrorCode.POST_ALREADY_RESERVED;

public class PostAlreadyReservedException extends GrpcErrorTypedClientException {

    private static final RemoteErrorCode ERROR_TYPE = POST_ALREADY_RESERVED;
    private static final String TEMPLATE = "пост id=%d пользователя userId=%d уже был зарезервирован ранее";

    public PostAlreadyReservedException(Long id, Long userId) {
        super(String.format(TEMPLATE, id, userId));
    }

    @Override
    public RemoteErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}

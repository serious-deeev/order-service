package org.serious.dev.exception;

import static org.serious.dev.exception.RemoteErrorCode.USER_NOT_FOUND;

public class NoSuchUserException extends GrpcErrorTypedClientException {

    private static final RemoteErrorCode ERROR_TYPE = USER_NOT_FOUND;
    private static final String TEMPLATE = "пользователь id %d не найден";

    public NoSuchUserException(Long id) {
        super(String.format(TEMPLATE, id));
    }

    @Override
    public RemoteErrorCode getErrorType() {
        return ERROR_TYPE;
    }
}

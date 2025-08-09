package org.serious.dev.exception;

public enum RemoteErrorCode {

    USER_NOT_FOUND,
    POST_NOT_FOUND,
    POST_ALREADY_RESERVED,
    POST_RESERVATION_ERROR,
    POST_CANCEL_ERROR;

    public static RemoteErrorCode fromCode(String code) {
        return valueOf(code);
    }
}

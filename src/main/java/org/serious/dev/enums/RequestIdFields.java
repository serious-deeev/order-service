package org.serious.dev.enums;

import lombok.Getter;

@Getter
public enum RequestIdFields {

    REQUEST_ID_HEADER("X-Request-Id"),
    REQUEST_ID_KEY("requestId");

    private final String value;

    RequestIdFields(String value) {
        this.value = value;
    }
}

package org.serious.dev.logging.util;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import static org.serious.dev.enums.RequestIdFields.REQUEST_ID_KEY;

@UtilityClass
public class RequestContextUtil {

    public static String getRequestId() {
        return MDC.get(REQUEST_ID_KEY.getValue());
    }
}

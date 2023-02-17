package com.food.common.error.exception;

import com.food.common.error.ApplicationErrors;
import com.food.common.error.CommonErrors;

public class InvalidRequestParameterException extends RuntimeException {
    private static final ApplicationErrors ERROR = CommonErrors.INVALID_REQUEST_PARAMETERS;

    public InvalidRequestParameterException(String extraMessage) {
        super(ERROR.appendMessage(extraMessage));
    }

    public String getErrorCode() {
        return ERROR.getCode();
    }
}

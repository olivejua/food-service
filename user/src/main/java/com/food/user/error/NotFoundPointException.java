package com.food.user.error;

import com.food.common.error.ApplicationErrors;

public class NotFoundPointException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.NOT_FOUND_POINT;

    public NotFoundPointException(Long pointId) {
        super(ERROR.appendMessage("pointId=" + pointId));
    }

    public NotFoundPointException() {
        super(ERROR.getMessage());
    }
}

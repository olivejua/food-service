package com.food.user.error;

import com.food.common.error.ApplicationErrors;

public class DuplicatePointRecollectException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.DUPLICATE_POINT_RECOLLECT;

    public DuplicatePointRecollectException(Long pointId) {
        super(ERROR.appendMessage("pointId=" + pointId));
    }
}

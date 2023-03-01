package com.food.user.error;

import com.food.common.error.ApplicationErrors;

public class DoesNotMatchPointOwnerException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.DOES_NOT_MATCH_POINT_OWNER;

    public DoesNotMatchPointOwnerException() {
        super(ERROR.getMessage());
    }
}

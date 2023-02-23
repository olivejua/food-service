package com.food.user.error;

import com.food.common.error.ApplicationErrors;
import com.food.user.error.PointErrors;

public class InvalidUsedPointsUnitException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.INVALID_USED_POINTS_UNIT;

    public InvalidUsedPointsUnitException() {
        super(ERROR.getMessage());
    }
}

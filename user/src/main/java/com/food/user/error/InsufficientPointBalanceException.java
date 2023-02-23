package com.food.user.error;

import com.food.common.error.ApplicationErrors;

public class InsufficientPointBalanceException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.INSUFFICIENT_POINT_BALANCE;

    public InsufficientPointBalanceException() {
        super(ERROR.getMessage());
    }
}

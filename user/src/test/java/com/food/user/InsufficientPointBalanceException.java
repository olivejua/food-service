package com.food.user;

import com.food.common.error.ApplicationErrors;
import com.food.user.error.PointErrors;

public class InsufficientPointBalanceException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.INSUFFICIENT_POINT_BALANCE;

    public InsufficientPointBalanceException() {
        super(ERROR.getMessage());
    }
}

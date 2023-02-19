package com.food.user.stub;

import com.food.common.error.ApplicationErrors;

public class NotFoundPaymentException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.NOT_FOUND_PAYMENT;

    public NotFoundPaymentException() {
        super(ERROR.getMessage());
    }
}

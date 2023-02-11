package com.food.order.temp;

import com.food.order.error.PaymentErrors;

public class DuplicatedPaymentException extends RuntimeException {
    private static final PaymentErrors ERROR = PaymentErrors.ALREADY_EXISTS_PAYMENT_FOR_ORDER;

    public DuplicatedPaymentException() {
        super(ERROR.getMessage());
    }
}

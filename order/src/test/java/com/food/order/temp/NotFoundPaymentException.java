package com.food.order.temp;

import com.food.order.error.PaymentErrors;

public class NotFoundPaymentException extends RuntimeException {
    private static final PaymentErrors ERROR = PaymentErrors.NOT_FOUND_PAYMENT;

    public NotFoundPaymentException(Long paymentId) {
        super(ERROR.appendMessage("paymentId=" + paymentId));
    }
}

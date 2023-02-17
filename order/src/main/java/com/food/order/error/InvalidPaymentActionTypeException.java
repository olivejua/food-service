package com.food.order.error;

public class InvalidPaymentActionTypeException extends RuntimeException {
    private static final PaymentErrors ERROR = PaymentErrors.INVALID_ACTION_TYPE;

    public InvalidPaymentActionTypeException(String detailMessage) {
        super(ERROR.appendMessage(detailMessage));
    }
}

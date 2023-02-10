package com.food.order.error;

public class InvalidPaymentDoParameterException extends RuntimeException {
    private static final PaymentErrors ERROR = PaymentErrors.INVALID_PAYMENT_PARAMETERS;

    public InvalidPaymentDoParameterException() {
    }

    public InvalidPaymentDoParameterException(String extraMessage) {
        super(ERROR.appendMessage(extraMessage));
    }

    public String getErrorCode() {
        return ERROR.getCode();
    }
}

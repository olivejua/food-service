package com.food.order.error;

public class InvalidPaymentException extends RuntimeException {
    private final PaymentErrors error;

    public InvalidPaymentException(PaymentErrors error) {
        super(error.getMessage());

        this.error = error;
    }
}

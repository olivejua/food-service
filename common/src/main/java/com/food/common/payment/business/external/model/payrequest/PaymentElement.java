package com.food.common.payment.business.external.model.payrequest;

import com.food.common.payment.enumeration.PaymentMethod;

import javax.validation.constraints.NotNull;

public abstract class PaymentElement {
    @NotNull
    protected final Integer amount;

    protected PaymentElement(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public abstract PaymentMethod method();

    public static PaymentElement findPaymentElement(PaymentMethod method, Integer amount) {
        return switch (method) {
            case CARD -> new CardPayment(amount);
            case ACCOUNT_TRANSFER -> new AccountTransferPayment(amount);
            case POINT -> new PointPayment(amount);
        };
    }
}

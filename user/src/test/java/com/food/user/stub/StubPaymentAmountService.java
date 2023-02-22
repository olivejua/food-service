package com.food.user.stub;

import com.food.common.payment.business.external.PaymentAmountService;
import com.food.common.utils.Amount;

public class StubPaymentAmountService implements PaymentAmountService {
    private Amount amount;

    public void remember(Amount amount) {
        this.amount = amount;
    }

    @Override
    public Amount sumOfPaymentAmountWithoutPoints(Long paymentId) {
        return amount;
    }
}

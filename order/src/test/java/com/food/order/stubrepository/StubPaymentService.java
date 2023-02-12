package com.food.order.stubrepository;

import com.food.common.payment.business.internal.PaymentCommonService;
import com.food.common.payment.enumeration.PaymentActionType;

public class StubPaymentService implements PaymentCommonService {
    private PaymentDto fakePayment;
    private boolean called = false;

    public void remember(PaymentDto fakePayment) {
        this.fakePayment = fakePayment;
    }

    @Override
    public Long save(Long orderId, PaymentActionType actionType) {
        return null;
    }

    @Override
    public void updateActionType(Long paymentId, PaymentActionType actionType) {

    }

    @Override
    public boolean existsById(Long id) {
        if (fakePayment != null) {
            return fakePayment.getId().equals(id);
        }

        return false;
    }
}

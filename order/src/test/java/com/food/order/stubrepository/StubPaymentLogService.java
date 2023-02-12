package com.food.order.stubrepository;

import com.food.common.payment.business.external.model.payrequest.PaymentElement;
import com.food.common.payment.business.internal.PaymentLogCommonService;
import com.food.common.payment.domain.PaymentLog;

import java.util.List;
import java.util.Set;

public class StubPaymentLogService implements PaymentLogCommonService {


    @Override
    public void saveAll(Long paymentId, Set<PaymentElement> elements) {

    }

    @Override
    public List<PaymentLog> findAllByPaymentId(Long paymentId) {
        return null;
    }
}

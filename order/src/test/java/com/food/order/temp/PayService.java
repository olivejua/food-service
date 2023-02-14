package com.food.order.temp;

import com.food.common.user.business.external.model.RequestUser;
import com.food.order.mock.MockRequestUser;
import com.food.order.presentation.dto.request.PaymentDoRequest;

public interface PayService {
    Long pay(PaymentDoRequest request, RequestUser requestUser);

    void cancel(Long paymentId, MockRequestUser mockRequestUser);
}

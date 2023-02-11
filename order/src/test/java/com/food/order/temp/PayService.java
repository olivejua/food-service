package com.food.order.temp;

import com.food.order.presentation.dto.request.PaymentDoRequest;

public interface PayService {
    Long pay(PaymentDoRequest request);
}

package com.food.order.temp;

import com.food.common.order.domain.Order;
import com.food.common.order.repository.OrderRepository;
import com.food.order.presentation.dto.request.PaymentDoRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultPayService implements PayService {
    private final OrderRepository orderRepository;

    @Override
    public void pay(PaymentDoRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundOrderException(request.getOrderId()));

    }
}

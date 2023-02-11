package com.food.order.stubrepository;

import com.food.common.order.business.internal.OrderCommonService;
import com.food.common.order.business.internal.dto.OrderDto;

import java.util.Optional;

public class StubOrderService implements OrderCommonService {

    private OrderDto fakeOrder;
    private boolean called = false;

    public void remember(OrderDto fakeOrder) {
        this.fakeOrder = fakeOrder;
    }

    @Override
    public Optional<OrderDto> findById(Long id) {
        if (fakeOrder != null && fakeOrder.getId().equals(id)) {
            return Optional.of(fakeOrder);
        }

        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return fakeOrder != null && fakeOrder.getId().equals(id);
    }

    @Override
    public OrderDto save(OrderDto order) {
        called = true;

        return order;
    }
}

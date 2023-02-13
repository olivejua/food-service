package com.food.order.stubrepository;

import com.food.common.order.business.internal.OrderCommonService;
import com.food.common.order.business.internal.dto.OrderDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StubOrderService implements OrderCommonService {
    private final Map<Long, OrderDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    @Override
    public Optional<OrderDto> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return data.containsKey(id);
    }

    @Override
    public OrderDto save(OrderDto order) {
        if (data.containsKey(order.getId())) {
            data.put(order.getId(), order);
            return order;
        }

        OrderDto newOne = OrderDto.builder()
                .id(autoIncrementKey--)
                .customerId(order.getCustomerId())
                .storeId(order.getStoreId())
                .amount(order.getAmount())
                .status(order.getStatus())
                .comment(order.getComment())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }
}

package com.food.order.order;

import com.food.common.error.exception.InvalidRequestParameterException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDoRequest {
    private final Long storeId;
    private final List<OrderMenuRequest> menus = new ArrayList<>();

    public OrderDoRequest(Long storeId, List<OrderMenuRequest> menus) {
        if (CollectionUtils.isEmpty(menus)) throw new InvalidRequestParameterException("주문메뉴는 하나이상 존재해야 합니다.");

        this.storeId = storeId;
        this.menus.addAll(menus);
    }

    public Long getStoreId() {
        return storeId;
    }

    public List<Long> getMenuIds() {
        return menus.stream()
                .map(OrderMenuRequest::getMenuId)
                .collect(Collectors.toList());
    }
}

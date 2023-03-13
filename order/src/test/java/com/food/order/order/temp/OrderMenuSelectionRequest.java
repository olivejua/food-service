package com.food.order.order.temp;

import com.food.common.error.exception.InvalidRequestParameterException;

public class OrderMenuSelectionRequest {
    public OrderMenuSelectionRequest(Long menuSelectionId, Integer count) {
        if (count <= 0) throw new InvalidRequestParameterException("주문메뉴의 수량은 1개 이상이어야 합니다.");
    }
}

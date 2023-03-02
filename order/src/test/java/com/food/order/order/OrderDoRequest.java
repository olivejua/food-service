package com.food.order.order;

import com.food.common.error.exception.InvalidRequestParameterException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderDoRequest {
    public OrderDoRequest(List<OrderMenuRequest> menus) {
        if (CollectionUtils.isEmpty(menus)) throw new InvalidRequestParameterException("주문메뉴는 하나이상 존재해야 합니다.");
    }
}

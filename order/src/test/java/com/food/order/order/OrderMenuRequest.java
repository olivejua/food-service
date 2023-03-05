package com.food.order.order;

import com.food.common.error.exception.InvalidRequestParameterException;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderMenuRequest {
    private final Long menuId;
    private final Integer count;
    private final List<OrderMenuSelectionRequest> selections = new ArrayList<>();

    public OrderMenuRequest(Long menuId, Integer count) {
        if (count <= 0) throw new InvalidRequestParameterException("주문메뉴의 수량은 1개 이상이어야 합니다.");

        this.menuId = menuId;
        this.count = count;
    }

    public OrderMenuRequest(Long menuId, Integer count, List<OrderMenuSelectionRequest> selections) {
        if (count <= 0) throw new InvalidRequestParameterException("주문메뉴의 수량은 1개 이상이어야 합니다.");

        this.menuId = menuId;
        this.count = count;
        addAll(selections);
    }

    private void addAll(List<OrderMenuSelectionRequest> addedSelections) {
        if (!CollectionUtils.isEmpty(addedSelections)) {
            this.selections.addAll(addedSelections);
        }
    }

    public Long getMenuId() {
        return menuId;
    }
}

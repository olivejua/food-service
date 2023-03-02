package com.food.order.order.mock;

import com.food.common.menu.business.internal.dto.MenuDto;
import com.food.common.utils.Amount;
import lombok.Builder;

public class MockMenu extends MenuDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockMenu(Long id, Long storeId, String name, Amount amount, Integer cookingMinutes) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.amount = amount;
        this.cookingMinutes = cookingMinutes;
    }
}

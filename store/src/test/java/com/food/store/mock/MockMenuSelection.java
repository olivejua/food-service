package com.food.store.mock;

import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import com.food.common.utils.Amount;
import lombok.Builder;

public class MockMenuSelection extends MenuSelectionDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockMenuSelection(Long id, Long optionId, String name, Amount amount) {
        this.id = id;
        this.optionId = optionId;
        this.name = name;
        this.amount = amount;
    }
}

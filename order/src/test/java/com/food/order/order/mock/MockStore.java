package com.food.order.order.mock;

import com.food.common.store.business.internal.dto.StoreDto;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.utils.Amount;
import lombok.Builder;

public class MockStore extends StoreDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockStore(Long id, String name, Amount minOrderAmount, OpenStatus status) {
        this.id = id;
        this.name = name;
        this.minOrderAmount = minOrderAmount;
        this.status = status;
    }
}

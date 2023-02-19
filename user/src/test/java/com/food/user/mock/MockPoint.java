package com.food.user.mock;

import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.enumeration.PointType;
import lombok.Builder;

public class MockPoint extends PointDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockPoint(Long id, Long userId, PointType type, Integer changedAmount, Integer currentAmount, Long paymentId) {
        super();
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.changedAmount = changedAmount;
        this.currentAmount = currentAmount;
        this.paymentId = paymentId;
    }
}

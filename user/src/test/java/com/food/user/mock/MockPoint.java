package com.food.user.mock;

import com.food.common.user.business.internal.dto.PointDto;
import com.food.common.user.enumeration.PointType;
import com.food.common.utils.Amount;
import lombok.Builder;

import java.time.LocalDateTime;

public class MockPoint extends PointDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockPoint(Long id, Long userId, PointType type, Amount changedAmount, Amount currentAmount, Long paymentId) {
        super();
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.changedAmount = changedAmount;
        this.currentAmount = currentAmount;
        this.paymentId = paymentId;
    }

    public void updateCreatedDate() {
        this.createdDate = LocalDateTime.now();
    }
}

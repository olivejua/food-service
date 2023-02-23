package com.food.common.user.business.external.model;

import com.food.common.user.business.internal.dto.PointSaveDto;
import com.food.common.user.enumeration.PointType;
import com.food.common.utils.Amount;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class PointUseRequest {
    @NotNull @Positive
    private final Amount amount;

    @NotNull
    private final Long ownerId;

    @Builder
    public PointUseRequest(Amount amount, Long ownerId) {
        this.amount = amount;
        this.ownerId = ownerId;
    }

    public boolean hasGreaterAmountThan(Amount currentAmount) {
        return amount.isGreaterThanOrEqualTo(currentAmount);
    }

    public PointSaveDto toPointSaveDto() {
        return PointSaveDto.builder()
                .usedId(ownerId)
                .amount(amount.getValue())
                .type(PointType.USE)
                .build();
    }
}

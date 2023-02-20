package com.food.common.user.business.internal.dto;

import com.food.common.user.domain.Point;
import com.food.common.user.enumeration.PointType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class PointDto {
    protected Long id;
    protected Long userId;
    protected PointType type;
    protected Integer changedAmount;
    protected Integer currentAmount;
    protected Long paymentId;

    public PointDto(@NotNull final Point point) {
        this.id = point.getId();
        this.userId = point.getUserId();
        this.type = point.getType();
        this.changedAmount = point.getChangedAmount();
        this.currentAmount = point.getCurrentAmount();
        this.paymentId = point.getPaymentId();
    }

    public static PointDto createBasePoint(@NotNull Long userId) {
        PointDto newInstance = new PointDto();
        newInstance.userId = userId;
        newInstance.currentAmount = 0;

        return newInstance;
    }

    public PointDto collect(@NotNull @Positive Integer amount, @NotNull Long paymentId) {
        PointDto point = new PointDto();
        point.userId = userId;
        point.type = PointType.COLLECT;
        point.changedAmount = amount;
        point.currentAmount = this.currentAmount - amount;
        point.paymentId = paymentId;

        return point;
    }
}

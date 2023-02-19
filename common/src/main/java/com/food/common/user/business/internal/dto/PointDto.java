package com.food.common.user.business.internal.dto;

import com.food.common.user.domain.Point;
import com.food.common.user.enumeration.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Getter
public final class PointDto {
    private Long id;
    private Long userId;
    private PointType type;
    private Integer changedAmount;
    private Integer currentAmount;
    private Long paymentId;

    public PointDto(@NotNull final Point point) {
        this.id = point.getId();
        this.userId = point.getUserId();
        this.type = point.getType();
        this.changedAmount = point.getChangedAmount();
        this.currentAmount = point.getCurrentAmount();
        this.paymentId = point.getPaymentId();
    }

    @Builder
    public PointDto(Long id, Long userId, PointType type, Integer changedAmount, Integer currentAmount, Long paymentId) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.changedAmount = changedAmount;
        this.currentAmount = currentAmount;
        this.paymentId = paymentId;
    }
}

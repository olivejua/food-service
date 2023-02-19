package com.food.common.user.business.internal.dto;

import com.food.common.user.domain.Point;
import com.food.common.user.enumeration.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

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

    @Builder(builderClassName = "SaveBuilder", builderMethodName = "saveBuilder")
    public PointDto(Long userId, PointType type, Integer changedAmount, Long paymentId) {
        Assert.notNull(userId, "userId must not be null");
        Assert.notNull(type, "type must not be null");
        Assert.notNull(changedAmount, "changedAmount must not be null");
        Assert.notNull(paymentId, "paymentId must not be null");

        this.userId = userId;
        this.type = type;
        this.changedAmount = changedAmount;
        this.paymentId = paymentId;
    }


}

package com.food.common.user.business.internal.dto;

import com.food.common.user.domain.Point;
import com.food.common.user.enumeration.PointType;
import com.food.common.utils.Amount;
import com.food.common.utils.UsedPoints;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class PointDto {
    protected Long id;
    protected Long userId;
    protected PointType type;
    protected Amount changedAmount;
    protected Amount currentAmount;
    protected Long paymentId;
    protected LocalDateTime createdDate;

    public PointDto(@NotNull final Point point) {
        this.id = point.getId();
        this.userId = point.getUserId();
        this.type = point.getType();
        this.changedAmount = point.getChangedAmount();
        this.currentAmount = point.getCurrentAmount();
        this.paymentId = point.getPaymentId();
        this.createdDate = point.getCreatedDate();
    }

    public static PointDto createBasePoint(@NotNull Long userId) {
        PointDto newInstance = new PointDto();
        newInstance.userId = userId;
        newInstance.currentAmount = Amount.zero();

        return newInstance;
    }

    public PointDto collect(@NotNull Amount collectAmount, @NotNull Long paymentId) {
        PointDto point = new PointDto();
        point.userId = userId;
        point.type = PointType.COLLECT;
        point.changedAmount = collectAmount;
        point.currentAmount = this.currentAmount.plus(collectAmount);
        point.paymentId = paymentId;

        return point;
    }

    public PointDto use(UsedPoints usedAmount) {
        PointDto point = new PointDto();
        point.userId = userId;
        point.type = PointType.COLLECT;
        point.changedAmount = usedAmount;
        point.currentAmount = this.currentAmount.subtract(usedAmount);
        point.paymentId = paymentId;

        return point;
    }

    public boolean hasSameOwnerIdAs(Long pointOwnerId) {
        return this.userId.equals(pointOwnerId);
    }
}

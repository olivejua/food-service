package com.food.common.payment.business.internal.model;

import com.food.common.payment.domain.Payment;
import com.food.common.payment.enumeration.PaymentActionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class PaymentDto {
    protected Long id;
    protected Long orderId;
    protected PaymentActionType actionType;

    @Builder
    public PaymentDto(Long id, Long orderId, PaymentActionType actionType) {
        this.id = id;
        this.orderId = orderId;
        this.actionType = actionType;
    }

    public PaymentDto(Payment payment) {
        Assert.notNull(payment, "payment must not be null.");

        id = payment.getId();
        orderId = payment.getOrder().getId();
        actionType = payment.getActionType();
    }

    public boolean isCanceled() {
        return actionType == PaymentActionType.CANCELLATION;
    }
}

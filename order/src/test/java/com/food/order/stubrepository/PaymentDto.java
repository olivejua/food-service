package com.food.order.stubrepository;

import com.food.common.payment.enumeration.PaymentActionType;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class PaymentDto {
    private final Long id;
    private final Long orderId;
    private final PaymentActionType actionType;

    @Builder
    public PaymentDto(Long id, Long orderId, PaymentActionType actionType) {
        this.id = id;
        this.orderId = orderId;
        this.actionType = actionType;
    }
}

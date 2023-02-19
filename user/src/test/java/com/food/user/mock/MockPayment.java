package com.food.user.mock;

import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.enumeration.PaymentActionType;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class MockPayment {
    public static Builder builder() {
        return new Builder();
    }

    public static PaymentDto create() {
        return builder()
                .build();
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Builder {
        private Long id;
        private PaymentActionType actionType = PaymentActionType.PAYMENT;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public PaymentDto build() {
            return PaymentDto.builder()
                    .id(id)
                    .orderId(1L)
                    .actionType(actionType)
                    .build();
        }
    }
}

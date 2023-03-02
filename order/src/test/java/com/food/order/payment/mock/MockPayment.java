package com.food.order.payment.mock;

import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.business.internal.model.PaymentDto;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class MockPayment {
    public static Builder builder() {
        return new Builder();
    }

    public static PaymentDto create(OrderDto order) {
        return builder()
                .order(order)
                .build();
    }

    public static PaymentDto create(OrderDto order, PaymentActionType actionType) {
        return builder()
                .order(order)
                .actionType(actionType)
                .build();
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Builder {
        private Long id;
        private OrderDto order;
        private PaymentActionType actionType = PaymentActionType.PAYMENT;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder order(OrderDto order) {
            this.order = order;
            return this;
        }

        public Builder actionType(PaymentActionType actionType) {
            this.actionType = actionType;
            return this;
        }

        public PaymentDto build() {
            return PaymentDto.builder()
                    .id(id)
                    .orderId(order != null ? order.getId() : null)
                    .actionType(actionType)
                    .build();
        }
    }
}

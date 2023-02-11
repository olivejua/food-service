package com.food.order.mock;

import com.food.common.order.domain.Order;
import com.food.common.payment.domain.Payment;
import com.food.common.payment.enumeration.PaymentActionType;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class MockPayment {
    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Builder {
        private Long id;
        private Order order;
        private PaymentActionType actionType = PaymentActionType.PAYMENT;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder order(Order order) {
            this.order = order;
            return this;
        }

        public Builder status(PaymentActionType actionType) {
            this.actionType = actionType;
            return this;
        }

        public Payment build() {
            return Payment.create(id, order, actionType);
        }
    }
}

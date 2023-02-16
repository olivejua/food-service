package com.food.order.mock;

import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.business.internal.model.PaymentLogDto;
import com.food.common.payment.enumeration.PaymentActionType;
import com.food.common.payment.enumeration.PaymentMethod;
import com.food.common.user.domain.Point;

public class MockPaymentLog {
    public static Builder builder() {
        return new Builder();
    }

    public static PaymentLogDto create(PaymentDto payment, PaymentMethod method, int amount) {
        return builder()
                .paymentId(payment.getId())
                .method(method)
                .amount(amount)
                .build();
    }

    public static class Builder {
        private Long id;
        private Long paymentId = 1L;
        private PaymentMethod method = PaymentMethod.CARD;
        private PaymentActionType type = PaymentActionType.PAYMENT;
        private Integer amount = 24000;
        private Point point;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder paymentId(Long id) {
            this.paymentId = id;
            return this;
        }

        public Builder actionType(PaymentActionType type) {
            this.type = type;
            return this;
        }

        public Builder method(PaymentMethod method) {
            this.method = method;
            return this;
        }

        public Builder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder point(Point point) {
            this.point = point;
            return this;
        }

        public PaymentLogDto build() {
            return PaymentLogDto.builder()
                    .paymentId(paymentId)
                    .method(method)
                    .amount(amount)
                    .build();
        }
    }
}

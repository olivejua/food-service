package com.food.order.payment.mock;

import com.food.common.order.business.internal.dto.OrderDto;
import com.food.common.order.enumeration.OrderStatus;
import com.food.common.store.domain.Store;
import com.food.common.user.domain.User;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

public class MockOrder {
    public static Builder builder() {
        return new Builder();
    }

    public static OrderDto create(int amount) {
        return builder()
                .amount(amount)
                .build();
    }

    public static OrderDto create() {
        return builder()
                .build();
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class Builder {
        private Long id;
        private User customer;
        private Store store;
        private Integer amount = 24000;
        private OrderStatus status = OrderStatus.COMPLETED;
        private String comment = "맛있게 해주세요";

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder customer(User customer) {
            this.customer = customer;
            return this;
        }

        public Builder store(Store store) {
            this.store = store;
            return this;
        }

        public Builder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public OrderDto build() {
            return OrderDto.builder()
                    .id(id)
                    .customerId(customer != null ? customer.getId() : null)
                    .storeId(store != null ? store.getId() : null)
                    .amount(amount)
                    .status(status)
                    .comment(comment)
                    .build();
        }
    }
}

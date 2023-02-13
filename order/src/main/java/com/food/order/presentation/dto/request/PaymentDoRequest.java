package com.food.order.presentation.dto.request;

import com.food.common.payment.enumeration.PaymentMethod;
import com.food.order.error.InvalidPaymentDoParameterException;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class PaymentDoRequest {
    private final Long orderId;
    private final List<Item> items = new ArrayList<>();

    public PaymentDoRequest(Long orderId, Item... items) {
        this.orderId = orderId;

        if (items != null) {
            this.items.addAll(Arrays.asList(items));
        }

        validate();
    }

    private void validate() {
        if (orderId == null) {
            throw new InvalidPaymentDoParameterException("주문정보가 비어있습니다.");
        }

        if (items.isEmpty()) {
            throw new InvalidPaymentDoParameterException("결제정보가 비어있습니다.");
        }

        if (isItemsWithDuplicatedMethods()) {
            throw new InvalidPaymentDoParameterException("중복된 결제수단은 존재할 수 없습니다.");
        }
    }

    private boolean isItemsWithDuplicatedMethods() {
        Set<PaymentMethod> methods = items.stream()
                .map(item -> item.method)
                .collect(Collectors.toSet());

        return methods.size() != items.size();
    }

    public Long getOrderId() {
        return orderId;
    }

    public boolean hasSameTotalAmountAs(Integer amount) {
        return totalPaymentAmount() == amount;
    }

    public int totalPaymentAmount() {
        return items.stream()
                .mapToInt(item -> item.amount)
                .sum();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean hasItemWithPointMethod() {
        return items.stream()
                .anyMatch(item -> item.getMethod() == PaymentMethod.POINT);
    }

    public int getUsedPointAmount() {
        return items.stream()
                .filter(item -> item.getMethod() == PaymentMethod.POINT)
                .map(Item::getAmount)
                .findAny()
                .orElse(0);
    }

    @Getter
    public static class Item {
        private final PaymentMethod method;
        private final Integer amount;

        public Item(PaymentMethod method, Integer amount) {
            this.method = method;
            this.amount = amount;
            validate();
        }

        private void validate() {
            if (amount == null) {
                throw new InvalidPaymentDoParameterException("결제금액이 비어있습니다.");
            }

            if (method == null) {
                throw new InvalidPaymentDoParameterException("결제수단이 비어있습니다.");
            }

            if (amount < 0) {
                throw new InvalidPaymentDoParameterException("결제금액은 양수여야 합니다.");
            }
        }
    }
}

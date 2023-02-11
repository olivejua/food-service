package com.food.order.error;

public class NotFoundOrderException extends RuntimeException {
    private static final OrderErrors ERROR = OrderErrors.NOT_FOUND_ORDER;

    public NotFoundOrderException(Long orderId) {
        super(ERROR.appendMessage("orderId=" + orderId));
    }
}

package com.food.order.order;

import com.food.common.error.ApplicationErrors;
import com.food.order.error.OrderErrors;

public class NotFoundMenuException extends RuntimeException {
    private static final ApplicationErrors ERROR = OrderErrors.NOT_FOUND_MENU_TO_ORDER;

    public NotFoundMenuException() {
        super(ERROR.getMessage());
    }
}

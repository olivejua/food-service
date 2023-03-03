package com.food.order.order;

import com.food.common.error.ApplicationErrors;
import com.food.order.error.OrderErrors;

public class InvalidStoreOpenStatusException extends RuntimeException {
    private static final ApplicationErrors ERROR = OrderErrors.INVALID_STORE_OPEN_STATUS;

    public InvalidStoreOpenStatusException() {
        super(ERROR.getMessage());
    }
}

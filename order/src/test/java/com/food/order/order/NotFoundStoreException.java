package com.food.order.order;

import com.food.common.error.ApplicationErrors;
import com.food.order.error.OrderErrors;

public class NotFoundStoreException extends RuntimeException {
    private static final ApplicationErrors ERROR = OrderErrors.NOT_FOUND_STORE_TO_ORDER;

    public NotFoundStoreException(Long storeId) {
        super(ERROR.appendMessage("storeId=" + storeId));
    }


}

package com.food.store.error;

import com.food.common.error.ApplicationErrors;

public class NotFoundStoreException extends RuntimeException {
    private static final ApplicationErrors ERROR = MenuErrors.NOT_FOUND_STORE_TO_FIND_MENUS;

    public NotFoundStoreException(Long storeId) {
        super(ERROR.appendMessage("storeId=" + storeId));
    }
}

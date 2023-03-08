package com.food.store.error;

import com.food.common.error.ApplicationErrors;

public class NotExistMenusException extends RuntimeException {
    private static final ApplicationErrors ERROR = MenuErrors.NOT_EXIST_MENUS;

    public NotExistMenusException() {
        super(ERROR.getMessage());
    }
}

package com.food.user.stub;

import com.food.common.error.ApplicationErrors;

public class NotFoundPointOwnerException extends RuntimeException {
    private static final ApplicationErrors ERROR = PointErrors.NOT_FOUND_OWNER;

    public NotFoundPointOwnerException(Long ownerId) {
        super(ERROR.appendMessage("ownerId="+ownerId));
    }
}

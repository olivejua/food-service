package com.food.user.stub;

import com.food.common.error.ApplicationErrors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointErrors implements ApplicationErrors {
    NOT_FOUND_OWNER("POINT_0001", "포인트 소유자를 찾을 수 없습니다."),
    NOT_FOUND_PAYMENT("POINT_0002", "포인트를 적립할 결제정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

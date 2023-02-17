package com.food.common.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrors implements ApplicationErrors {
    INVALID_REQUEST_PARAMETERS("COMMON_0001", "유효하지 않은 파라미터 요청입니다."),
    ;

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}

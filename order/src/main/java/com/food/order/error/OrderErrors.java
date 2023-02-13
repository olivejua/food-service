package com.food.order.error;

import com.food.common.error.ApplicationErrors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderErrors implements ApplicationErrors {
    NOT_FOUND_ORDER("ORDER_0001", "주문서를 찾을 수 없습니다.")
    ;

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

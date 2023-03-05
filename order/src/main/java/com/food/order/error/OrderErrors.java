package com.food.order.error;

import com.food.common.error.ApplicationErrors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderErrors implements ApplicationErrors {
    NOT_FOUND_ORDER("ORDER_0001", "주문서를 찾을 수 없습니다."),
    NOT_FOUND_STORE_TO_ORDER("ORDER_0002", "주문할 가게를 찾을 수 없습니다."),
    INVALID_STORE_OPEN_STATUS("ORDER_0003", "운영종료된 가게는 주문을 받을 수 없습니다."),
    NOT_FOUND_MENU_TO_ORDER("ORDER_0004", "주문할 메뉴를 찾을 수 없습니다."),
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

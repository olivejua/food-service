package com.food.order.error;

import com.food.common.error.ApplicationErrors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentErrors implements ApplicationErrors {
    WRONG_PAYMENT_AMOUNT("PAYMENT_0001", "결제금액이 주문금액과 동일하지 않습니다."),
    ALREADY_EXISTS_PAYMENT_FOR_ORDER("PAYMENT_0002", "해당 주문서에 대한 결제내역이 존재합니다."),
    NOT_FOUND_PAYMENT("PAYMENT_0003", "결제정보를 찾을 수 없습니다."),
    INVALID_ACTION_TYPE("PAYMENT_0004", "적합하지 않은 액션타입의 결제정보입니다."),
    ;

    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

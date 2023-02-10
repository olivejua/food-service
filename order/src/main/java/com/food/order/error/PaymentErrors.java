package com.food.order.error;

import com.food.common.error.ApplicationErrors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentErrors implements ApplicationErrors {
    INVALID_PAYMENT_PARAMETERS("P001", "유효하지 않은 파라미터 요청으로 인해 결제를 수행할 수 없습니다."),
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

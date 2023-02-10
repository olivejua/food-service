package com.food.order.error;

import com.food.common.error.ApplicationErrors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentErrors implements ApplicationErrors {
    INVALID_PAYMENT_PARAMETERS("PAYMENT_0001", "유효하지 않은 파라미터 요청으로 인해 결제를 수행할 수 없습니다."),
    WRONG_PAYMENT_AMOUNT("PAYMENT_0002", "결제금액이 주문금액과 동일하지 않습니다.")
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

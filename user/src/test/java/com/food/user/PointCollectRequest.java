package com.food.user;

import com.food.common.error.exception.InvalidRequestParameterException;
import lombok.Getter;

@Getter
public class PointCollectRequest {
    private final Long ownerId;
    private final Integer amount;
    private final Long paymentId;

    public PointCollectRequest(Long ownerId, Integer amount, Long paymentId) {
        this.ownerId = ownerId;
        this.amount = amount;
        this.paymentId = paymentId;

        validate();
    }

    private void validate() {
        if (amount <= 0) {
            throw new InvalidRequestParameterException("포인트 적립금액은 0이상의 금액이어야 합니다.");
        }

        if (amount % 10 != 0) {
            throw new InvalidRequestParameterException("포인트 적립금액은 10원 단위의 금액이어야 합니다.");
        }

        if (paymentId == null) {
            throw new InvalidRequestParameterException("PaymentId는 비어있을 수 없습니다.");
        }
    }
}

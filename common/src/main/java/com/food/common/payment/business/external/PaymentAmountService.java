package com.food.common.payment.business.external;

import com.food.common.utils.Amount;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface PaymentAmountService {
    /**
     * 주어진 결제 ID의 총 결제 금액을 반환한다. (포인트 사용금액 제외)
     * @param paymentId 결제 ID
     * @return 결제금액. null일 수 없다.
     */
    Amount sumOfPaymentAmountWithoutPoints(@NotNull Long paymentId);
}

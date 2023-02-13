package com.food.common.payment.business.internal;

import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.enumeration.PaymentActionType;

import javax.validation.constraints.NotNull;

public interface PaymentCommonService {
    /**
     * 결제데이터를 저장하고, 새로운 ID가 부여된 Dto 객체를 리턴한다.
     * @param paymentDto 저장될 결제데이터. null일 수 없다.
     * @return 저장된 결제 데이터가 담긴 Dto 객체
     */
    PaymentDto save(@NotNull PaymentDto paymentDto);

    void updateActionType(@NotNull Long paymentId, @NotNull PaymentActionType actionType);

    /**
     * PaymentId로 결제데이터의 존재유무를 리턴한다.
     * @param id 검색할 ID. null일 수 없다.
     * @return 데이터 존재 유무
     */
    boolean existsById(@NotNull Long id);
}

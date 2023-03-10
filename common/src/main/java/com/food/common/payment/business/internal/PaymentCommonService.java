package com.food.common.payment.business.internal;

import com.food.common.payment.business.internal.model.PaymentDto;
import com.food.common.payment.enumeration.PaymentActionType;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface PaymentCommonService {
    /**
     * 결제데이터를 저장하고, 새로운 ID가 부여된 Dto 객체를 리턴한다.
     * @param paymentDto 저장될 결제데이터. null일 수 없다.
     * @return 저장된 결제 데이터가 담긴 Dto 객체
     */
    PaymentDto save(@NotNull PaymentDto paymentDto);

    /**
     * 주어진 결제 Id를 가진 결제 데이터를 액션타입을 주어진 actionType값으로 변경한다.
     * @param paymentId 결제 ID. null일 수 없다.
     * @param actionType 변경할 액션타입 (결제 또는 취소). null일 수 없다.
     */
    void updateActionType(@NotNull Long paymentId, @NotNull PaymentActionType actionType);

    /**
     * PaymentId로 결제데이터의 존재유무를 리턴한다.
     * @param id 검색할 ID. null일 수 없다.
     * @return 데이터 존재 유무
     */
    boolean existsById(@NotNull Long id);

    /**
     * ID로 결제데이터를 리턴한다.
     * @param id 결제 ID. null일 수 없다.
     * @return 주어진 ID를 보유한 결제데이터 또는 찾을 수 없는 경우 Optional#empty()
     */
    Optional<PaymentDto> findById(@NotNull Long id);
}

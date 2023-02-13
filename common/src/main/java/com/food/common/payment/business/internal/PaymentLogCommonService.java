package com.food.common.payment.business.internal;

import com.food.common.payment.business.external.model.payrequest.PaymentElement;
import com.food.common.payment.business.internal.model.PaymentLogDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public interface PaymentLogCommonService {
    /**
     * 주어진 결제로그데이터 객체들을 모두 저장한다.
     * @param paymentId 공통으로 주입될 결제데이터 ID. null일 수 없다.
     * @param elements 결제수단과 결제금액 정보가 포함되어 있는 객체 목록. null일 수 없으며, 1개 이상이어야 한다.
     */
    void saveAll(@NotNull Long paymentId, @NotNull @Size(min = 1) Set<PaymentElement> elements);

    /**
     * 주어진 결제ID로 결제로그 데이터 목록을 조회한다.
     * @param paymentId 결제데이터 ID
     * @return 주어진 결제데이터 ID를 가진 결제로그 데이터 목록. 리턴 객체는 null일 수 없다.
     */
    List<PaymentLogDto> findAllByPaymentId(@NotNull Long paymentId);
}

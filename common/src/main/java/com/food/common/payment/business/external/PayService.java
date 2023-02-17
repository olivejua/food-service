package com.food.common.payment.business.external;

import com.food.common.payment.business.external.model.PaymentDoRequest;
import com.food.common.user.business.external.model.RequestUser;
import org.springframework.validation.annotation.Validated;

@Validated
public interface PayService {
    /**
     * 주문건의 결제정보를 저장하고, 결제정보의 ID를 리턴한다.
     * @param request 주문건과 결제 수단을 포함한 요청 정보. null일 수 없다.
     * @param requestUser 결제 요청자. null일 수 없다.
     * @return 결제정보 ID
     */
    Long pay(PaymentDoRequest request, RequestUser requestUser);

    /**
     * 결제정보 취소를 저장한다.
     * 취소하는 결제정보에 포인트 사용/적립 존재유무에 따라 재적립/회수절차가 포함된다.
     * @param paymentId 결제정보 ID. null일 수 없다.
     * @param requestUser 결제 취소 요청자. null일 수 없다.
     */
    void cancel(Long paymentId, RequestUser requestUser);
}

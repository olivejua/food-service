package com.food.user.stub;

import com.food.common.user.business.external.model.RequestUser;
import com.food.common.utils.UsedPoints;
import com.food.user.error.NotFoundPointOwnerException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface PointService {
    /**
     * 포인트를 적립힌다.
     * @param paymentId 적립 대상 결제데이터 ID
     * @param requestUser 적립 대상 소유자. 현재 로그인한 유저가 적립 대상이 된다.
     * @throws NotFoundPointOwnerException 포인트 소유자를 찾을 수 없는 경우 발생
     */
    void collect(@NotNull Long paymentId, @NotNull RequestUser requestUser);

    /**
     * 포인트를 사용한다.
     * @param amount 사용 포인트 금액이 담긴 객체
     * @param requestUser 포인트 차감 대상 소유자. 현재 로그인한 유저가 포인트 차감 대상이 된다.
     * @return
     */
    Long use(@NotNull UsedPoints amount, @NotNull RequestUser requestUser);


}

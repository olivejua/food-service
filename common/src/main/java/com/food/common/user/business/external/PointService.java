package com.food.common.user.business.external;

import com.food.common.user.business.external.model.RequestUser;
import com.food.common.utils.UsedPoints;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface PointService {
    /**
     * 포인트를 적립힌다.
     * @param paymentId 적립 대상 결제데이터 ID
     * @param requestUser 적립 대상 소유자. 현재 로그인한 유저가 적립 대상이 된다.
     */
    void collect(@NotNull Long paymentId, @NotNull RequestUser requestUser);

    /**
     * 포인트를 사용한다.
     * @param amount 사용 포인트 금액이 담긴 객체
     * @param requestUser 포인트 차감 대상 소유자. 현재 로그인한 유저가 포인트 차감 대상이 된다.
     * @return 사용 포인트 ID
     */
    Long use(@NotNull UsedPoints amount, @NotNull RequestUser requestUser);

    /**
     * 사용했던 포인트를 재적립한다.
     * @param pointId 사용포인트 ID
     * @param requestUser 포인트 재적립 대상 소유자. 현재 로그인한 유저가 포인트 재적립 대상이 된다.
     * @return 재적립 포인트 ID
     */
    Long recollect(@NotNull Long pointId, @NotNull RequestUser requestUser);

    /**
     * 주어진 결제ID로 적립된 포인트 금액을 회수한다.
     * @param paymentId 적립된 결제 ID
     * @param requestUser 포인트 회수 대상 소유자. 현재 로그인한 유저가 포인트 재적립 대상이 된다.
     * @return 회수 포인트 ID
     */
    Long retrieve(@NotNull Long paymentId, @NotNull RequestUser requestUser);
}


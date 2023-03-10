package com.food.common.user.business.internal;

import com.food.common.user.business.internal.dto.PointDto;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface PointCommonService {
    /**
     * 포인트 데이터를 저장하고, 새로운 ID가 부여된 Dto 객체를 리턴한다.
     * @param pointDto 저장될 포인트 데이터. null일 수 없다.
     * @return 저장된 포인트 데이터가 담긴 Dto 객체
     */
    PointDto save(@NotNull PointDto pointDto);

    /**
     * 주어진 소유자의 가장 최근 포인트데이터를 리턴한다.
     * @param userId 소유자 ID
     * @return 주어진 소유자 ID를 보유한 가장 최근 포인트 데이터 또는 찾을 수 없는 경우 Optional#empty()
     */
    Optional<PointDto> findLatestPointByUserId(@NotNull Long userId);

    /**
     * ID로 포인트데이터를 리턴한다.
     * @param pointId 포인트 ID. null일 수 없다.
     * @return 주어진 ID를 보유한 포인트데이터 또는 찾을 수 없는 경우 Optional#empty()
     */
    Optional<PointDto> findByPointId(@NotNull Long pointId);

    /**
     * 주어진 결제ID의 포인트 데이터를 리턴한다. (다수일 경우 최신순)
     * @param paymentId 결제 ID. null일 수 없다.
     * @return 주어진 결제ID를 보유한 포인트데이터 또는 찾을 수 없는 경우 Optional#empty()
     */
    Optional<PointDto> findByPaymentId(@NotNull Long paymentId);
}

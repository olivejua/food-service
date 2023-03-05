package com.food.common.store.business.internal;

import com.food.common.store.business.internal.dto.StoreDto;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface StoreCommonService {
    /**
     * ID로 가게데이터를 리턴한다.
     * @param storeId 가게 ID. null일 수 없다.
     * @return 주어진 ID를 보유한 가게데이터 또는 찾을 수 없는 경우 Optional#empty()
     */
    Optional<StoreDto> findById(@NotNull Long storeId);

    /**
     * StoreId로 가게데이터의 존재유무를 리턴한다.
     * @param storeId 검색할 ID. null일 수 없다.
     * @return 데이터 존재 유무
     */
    boolean existsById(@NotNull Long storeId);
}

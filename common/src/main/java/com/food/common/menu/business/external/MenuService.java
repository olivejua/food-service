package com.food.common.menu.business.external;

import com.food.common.menu.business.external.dto.StoreMenus;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface MenuService {
    /**
     * 주어진 가게 ID의 가게데이터에 포함된 전체 메뉴목록을 조회한다.
     * @param storeId 검색할 가게 ID. null일 수 없다.
     * @return 검색한 가게의 메뉴데이터가 포함된 Dto. null일 수 없다.
     */
    StoreMenus findAllMenusByStoreId(@NotNull Long storeId);
}

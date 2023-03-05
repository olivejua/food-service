package com.food.store.business;

import com.food.common.menu.business.external.MenuService;
import com.food.common.menu.business.external.dto.StoreMenus;
import com.food.common.menu.business.internal.MenuCommonService;
import com.food.common.store.business.internal.StoreCommonService;
import com.food.store.error.NotFoundStoreException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultMenuService implements MenuService {
    private final StoreCommonService storeCommonService;
    private final MenuCommonService menuCommonService;

    @Override
    public StoreMenus findAllMenusByStoreId(Long storeId) {
        validateIfStoreExists(storeId);

        return null;
    }

    private void validateIfStoreExists(Long storeId) {
        if (!storeCommonService.existsById(storeId)) {
            throw new NotFoundStoreException(storeId);
        }
    }
}

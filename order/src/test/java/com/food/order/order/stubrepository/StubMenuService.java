package com.food.order.order.stubrepository;

import com.food.common.menu.business.external.MenuService;
import com.food.common.menu.business.external.dto.StoreMenus;

public class StubMenuService implements MenuService {
    private StoreMenus mockStoreMenus;

    public void remember(StoreMenus storeMenus) {
        this.mockStoreMenus = storeMenus;
    }

    @Override
    public StoreMenus findAllMenusByStoreId(Long storeId) {
        return mockStoreMenus;
    }
}

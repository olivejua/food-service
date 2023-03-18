package com.food.order.order.mock;

import com.food.common.menu.business.external.dto.StoreMenuItem;
import com.food.common.menu.business.external.dto.StoreMenus;

import java.util.ArrayList;
import java.util.List;

public class MockStoreMenus implements StoreMenus {
    private Long storeId;
    private List<StoreMenuItem> menus = new ArrayList<>();

    public static MockStoreMenus create(Long storeId, List<StoreMenuItem> menus) {
        MockStoreMenus newInstance = new MockStoreMenus();
        newInstance.storeId = storeId;
        newInstance.menus.addAll(menus);

        return newInstance;
    }

    @Override
    public Long getStoreId() {
        return storeId;
    }

    @Override
    public List<StoreMenuItem> getMenus() {
        return menus;
    }

    @Override
    public boolean containsAll(List<Long> menuIds) {
        return menus.stream()
                .allMatch(menu -> menuIds.contains(menu.getId()));
    }
}

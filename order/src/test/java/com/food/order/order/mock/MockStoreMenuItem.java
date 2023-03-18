package com.food.order.order.mock;

import com.food.common.menu.business.external.dto.StoreMenuItem;
import com.food.common.menu.business.external.dto.StoreMenuOptionItem;
import com.food.common.utils.Amount;

import java.util.List;

public class MockStoreMenuItem implements StoreMenuItem {
    private MockMenu mockMenu;
    private List<StoreMenuOptionItem> options;

    public MockStoreMenuItem(MockMenu mockMenu, List<StoreMenuOptionItem> options) {
        this.mockMenu = mockMenu;
        this.options = options;
    }

    public static MockStoreMenuItem mock(MockMenu menu, List<StoreMenuOptionItem> options) {
        return new MockStoreMenuItem(menu, options);
    }

    @Override
    public Long getId() {
        return mockMenu.getId();
    }

    @Override
    public Amount getAmount() {
        return mockMenu.getAmount();
    }

    @Override
    public Integer getCookingMinutes() {
        return mockMenu.getCookingMinutes();
    }

    @Override
    public List<StoreMenuOptionItem> getOptions() {
        return options;
    }
}

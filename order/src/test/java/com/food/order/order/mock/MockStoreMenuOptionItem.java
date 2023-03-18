package com.food.order.order.mock;

import com.food.common.menu.business.external.dto.StoreMenuOptionItem;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;

import java.util.List;

public class MockStoreMenuOptionItem implements StoreMenuOptionItem {
    private MockMenuOption option;
    private List<MenuSelectionDto> selections;

    public MockStoreMenuOptionItem(MockMenuOption option, List<MenuSelectionDto> selections) {
        this.option = option;
        this.selections = selections;
    }

    public static MockStoreMenuOptionItem mock(MockMenuOption option, List<MenuSelectionDto> selections) {
        return new MockStoreMenuOptionItem(option, selections);
    }

    @Override
    public Long getId() {
        return option.getId();
    }

    @Override
    public String getName() {
        return option.getName();
    }

    @Override
    public Byte getMinSize() {
        return option.getMinSize();
    }

    @Override
    public Byte getMaxSize() {
        return option.getMaxSize();
    }

    @Override
    public List<MenuSelectionDto> getSelections() {
        return selections;
    }
}

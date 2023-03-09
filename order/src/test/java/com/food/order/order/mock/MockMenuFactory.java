package com.food.order.order.mock;

import com.food.common.utils.Amount;
import com.food.common.utils.ByteUtils;

public class MockMenuFactory {
    private static long autoIncrementKeyOfMenu = 0;
    private static long autoIncrementKeyOfOption = 0;
    private static long autoIncrementKeyOfSelection = 0;

    public static MockMenu mockMenu(Long storeId, String name, Integer amount) {
        return MockMenu.testBuilder()
                .id(--autoIncrementKeyOfMenu)
                .storeId(storeId)
                .name(name)
                .amount(Amount.won(amount))
                .build();
    }

    public static MockMenuOption mockOption(Long menuId, String name, Integer minSize, Integer maxSize) {
        return MockMenuOption.testBuilder()
                .id(--autoIncrementKeyOfOption)
                .menuId(menuId)
                .name(name)
                .minSize(ByteUtils.byteValue(minSize))
                .minSize(ByteUtils.byteValue(maxSize))
                .build();
    }

    public static MockMenuSelection mockSelection(Long optionId, String name, Integer amount) {
        return MockMenuSelection.testBuilder()
                .id(--autoIncrementKeyOfSelection)
                .optionId(optionId)
                .name(name)
                .amount(Amount.won(amount))
                .build();
    }
}

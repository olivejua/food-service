package com.food.common.menu.business.external.dto;

import com.food.common.menu.business.internal.dto.MenuSelectionDto;

import java.util.List;

public interface StoreMenuOptionItem {
    Long getId();

    String getName();

    Byte getMinSize();

    Byte getMaxSize();

    List<MenuSelectionDto> getSelections();
}

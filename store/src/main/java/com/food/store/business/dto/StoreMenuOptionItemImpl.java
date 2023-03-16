package com.food.store.business.dto;

import com.food.common.menu.business.external.dto.StoreMenuOptionItem;
import com.food.common.menu.business.internal.dto.MenuOptionDto;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StoreMenuOptionItemImpl implements StoreMenuOptionItem {
    private final MenuOptionDto option;
    private final List<MenuSelectionDto> selections = new ArrayList<>();

    public StoreMenuOptionItemImpl(MenuOptionDto option, List<MenuSelectionDto> selections) {
        this.option = option;

        if (!CollectionUtils.isEmpty(selections)) {
            this.selections.addAll(selections);
        }
    }

    public Long getId() {
        return option.getId();
    }

    public String getName() {
        return option.getName();
    }

    public Byte getMinSize() {
        return option.getMinSize();
    }

    public Byte getMaxSize() {
        return option.getMaxSize();
    }
}

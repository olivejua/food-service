package com.food.store.business.dto;

import com.food.common.menu.business.external.dto.StoreMenuItem;
import com.food.common.menu.business.external.dto.StoreMenuOptionItem;
import com.food.common.menu.business.internal.dto.MenuDto;
import com.food.common.utils.Amount;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StoreMenuItemImpl implements StoreMenuItem {
    private final MenuDto menu;
    private final List<StoreMenuOptionItem> options = new ArrayList<>();

    public StoreMenuItemImpl(MenuDto menu, List<StoreMenuOptionItem> options) {
        this.menu = menu;
        if (!CollectionUtils.isEmpty(options)) {
            this.options.addAll(options);
        }
    }

    public Long getId() {
        return menu.getId();
    }

    public Amount getAmount() {
        return menu.getAmount();
    }

    public Integer getCookingMinutes() {
        return menu.getCookingMinutes();
    }

    public List<StoreMenuOptionItem> getOptions() {
        return options;
    }
}

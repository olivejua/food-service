package com.food.store.business.dto;

import com.food.common.menu.business.external.dto.StoreMenuItem;
import com.food.common.menu.business.external.dto.StoreMenuOptionItem;
import com.food.common.menu.business.external.dto.StoreMenus;
import com.food.common.menu.business.internal.dto.*;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class StoreMenusImpl implements StoreMenus {
    private final Long storeId;
    private final List<StoreMenuItem> menus = new ArrayList<>();

    public StoreMenusImpl(Long storeId, List<MenuDto> menus, Map<Long, List<MenuOptionDto>> options, Map<Long, List<MenuSelectionDto>> selections) {
        Assert.notNull(storeId, "storeId must not be null.");
        Assert.notNull(menus, "menus must not be null.");
        Assert.isTrue(!menus.isEmpty(), "menus must not be empty.");

        this.storeId = storeId;
        addAll(menus, options, selections);
    }

    private void addAll(List<MenuDto> menus, Map<Long, List<MenuOptionDto>> options, Map<Long, List<MenuSelectionDto>> selections) {
        for (MenuDto menu : menus) {
            List<StoreMenuOptionItem> mappedOptions = mapToMenuOptionDtoWithRelations(options.get(menu.getId()), selections);
            this.menus.add(new StoreMenuItemImpl(menu, mappedOptions));
        }
    }

    private List<StoreMenuOptionItem> mapToMenuOptionDtoWithRelations(List<MenuOptionDto> options, Map<Long, List<MenuSelectionDto>> selections) {
        if (CollectionUtils.isEmpty(options)) {
            return Collections.emptyList();
        }

        return options.stream()
                .map(option -> new StoreMenuOptionItemImpl(option, selections.get(option.getId())))
                .collect(Collectors.toList());
    }

    public boolean containsAll(List<Long> menuIds) {
        if (menuIds == null) return false;

        for (Long menuId : menuIds) {
            if (!contains(menuId)) {
                return false;
            }
        }

        return true;
    }

    private boolean contains(Long menuId) {
        return menus.stream().anyMatch(menu -> menu.getId().equals(menuId));
    }
}

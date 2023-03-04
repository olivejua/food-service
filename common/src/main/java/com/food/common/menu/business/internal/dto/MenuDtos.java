package com.food.common.menu.business.internal.dto;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class MenuDtos {
    private final List<MenuDto> menus = new ArrayList<>();

    public MenuDtos(List<MenuDto> menus) {
        Assert.notNull(menus);

        this.menus.addAll(menus);
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

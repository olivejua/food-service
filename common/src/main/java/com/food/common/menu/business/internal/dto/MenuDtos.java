package com.food.common.menu.business.internal.dto;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuDtos {
    private final List<MenuDto> menus = new ArrayList<>();

    public MenuDtos(List<MenuDto> menus) {
        Assert.notNull(menus);

        this.menus.addAll(menus);
    }


    public boolean isEmpty() {
        return menus.isEmpty();
    }

    public Set<Long> mapToMenuIds() {
        return menus.stream()
                .map(MenuDto::getId)
                .collect(Collectors.toSet());
    }

    public List<MenuDto> get() {
        return Collections.unmodifiableList(menus);
    }
}

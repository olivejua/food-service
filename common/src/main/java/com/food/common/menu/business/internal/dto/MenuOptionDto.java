package com.food.common.menu.business.internal.dto;

import com.food.common.menu.domain.MenuOption;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MenuOptionDto {
    private Long id;
    private Long menuId;
    private String name;
    private Byte minSize;
    private Byte maxSize;

    public MenuOptionDto(@NotNull MenuOption entity) {
        this.id = entity.getId();
        this.menuId = entity.getMenuId();
        this.name = entity.getName();
        this.minSize = entity.getMinSize();
        this.maxSize = entity.getMaxSize();
    }
}

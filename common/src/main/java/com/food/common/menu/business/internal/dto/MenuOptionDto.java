package com.food.common.menu.business.internal.dto;

import com.food.common.menu.domain.MenuOption;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MenuOptionDto {
    protected Long id;
    protected Long menuId;
    protected String name;
    protected Byte minSize;
    protected Byte maxSize;

    public MenuOptionDto(@NotNull MenuOption entity) {
        this.id = entity.getId();
        this.menuId = entity.getMenuId();
        this.name = entity.getName();
        this.minSize = entity.getMinSize();
        this.maxSize = entity.getMaxSize();
    }
}

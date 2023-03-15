package com.food.common.menu.business.external.dto;

import com.food.common.menu.business.internal.dto.MenuOptionDtoWithRelations;
import com.food.common.utils.Amount;

import java.util.List;

public interface StoreMenuItem {
    Long getId();

    Amount getAmount();

    Integer getCookingMinutes();

    List<MenuOptionDtoWithRelations> getOptions();
}

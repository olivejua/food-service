package com.food.common.menu.business.external.dto;

import com.food.common.menu.business.internal.dto.MenuDtoWithRelations;

import java.util.List;

public interface StoreMenus {
    Long getStoreId();
    List<MenuDtoWithRelations> getMenus();
    boolean containsAll(List<Long> menuIds);
}

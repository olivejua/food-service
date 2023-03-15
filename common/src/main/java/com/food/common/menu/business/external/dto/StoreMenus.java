package com.food.common.menu.business.external.dto;

import java.util.List;

public interface StoreMenus {
    Long getStoreId();
    List<StoreMenuItem> getMenus();
    boolean containsAll(List<Long> menuIds);
}

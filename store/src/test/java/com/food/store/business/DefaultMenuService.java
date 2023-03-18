package com.food.store.business;

import com.food.common.menu.business.external.MenuService;
import com.food.store.business.dto.StoreMenusImpl;
import com.food.common.menu.business.internal.MenuCommonService;
import com.food.common.menu.business.internal.MenuOptionCommonService;
import com.food.common.menu.business.internal.MenuSelectionCommonService;
import com.food.common.menu.business.internal.dto.MenuDtos;
import com.food.common.menu.business.internal.dto.MenuOptionDto;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import com.food.common.store.business.internal.StoreCommonService;
import com.food.store.error.NotExistMenusException;
import com.food.store.error.NotFoundStoreException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultMenuService implements MenuService {
    private final StoreCommonService storeCommonService;
    private final MenuCommonService menuCommonService;
    private final MenuOptionCommonService menuOptionCommonService;
    private final MenuSelectionCommonService menuSelectionCommonService;

    @Override
    public StoreMenusImpl findAllMenusByStoreId(Long storeId) {
        validateIfStoreExists(storeId);

        MenuDtos menus = menuCommonService.findAllByStoreId(storeId);
        if (menus.isEmpty()) {
            throw new NotExistMenusException();
        }

        Map<Long, List<MenuOptionDto>> menuOptions = menuOptionCommonService.findAllByMenuIds(menus.mapToMenuIds());
        Map<Long, List<MenuSelectionDto>> menuSelections = menuSelectionCommonService.findAllByMenuOptionIds(menuOptions.keySet());

        return new StoreMenusImpl(storeId, menus.get(), menuOptions, menuSelections);
    }

    private void validateIfStoreExists(Long storeId) {
        if (!storeCommonService.existsById(storeId)) {
            throw new NotFoundStoreException(storeId);
        }
    }
}

package com.food.store.stub;

import com.food.common.menu.business.internal.MenuCommonService;
import com.food.common.menu.business.internal.dto.MenuDto;
import com.food.common.menu.business.internal.dto.MenuDtos;
import com.food.store.mock.MockMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StubMenuCommonService implements MenuCommonService {
    private final Map<Long, MenuDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    public MenuDto save(MenuDto menu) {
        if (data.containsKey(menu.getId())) {
            data.put(menu.getId(), menu);
            return menu;
        }

        MockMenu newOne = MockMenu.testBuilder()
                .id(autoIncrementKey--)
                .storeId(menu.getStoreId())
                .name(menu.getName())
                .amount(menu.getAmount())
                .cookingMinutes(menu.getCookingMinutes())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    @Override
    public List<MenuDto> saveAll(List<MenuDto> menus) {
        List<MenuDto> result = new ArrayList<>();

        for (MenuDto menu : menus) {
            result.add(save(menu));
        }

        return result;
    }

    @Override
    public MenuDtos findAllByStoreId(Long storeId) {
        List<MenuDto> menus = data.values().stream()
                .filter(menu -> menu.getStoreId().equals(storeId))
                .collect(Collectors.toList());

        return new MenuDtos(menus);
    }
}

package com.food.store.stub;

import com.food.common.menu.business.internal.MenuOptionCommonService;
import com.food.common.menu.business.internal.dto.MenuOptionDto;
import com.food.store.mock.MockMenuOption;

import java.util.*;

public class StubMenuOptionCommonService implements MenuOptionCommonService {
    private final Map<Long, MenuOptionDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    public MenuOptionDto save(MenuOptionDto menuOption) {
        if (data.containsKey(menuOption.getId())) {
            data.put(menuOption.getId(), menuOption);
            return menuOption;
        }

        MockMenuOption newOne = MockMenuOption.testBuilder()
                .id(autoIncrementKey--)
                .menuId(menuOption.getMenuId())
                .name(menuOption.getName())
                .minSize(menuOption.getMinSize())
                .maxSize(menuOption.getMaxSize())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    public List<MenuOptionDto> saveAll(List<MenuOptionDto> menuOptions) {
        List<MenuOptionDto> result = new ArrayList<>();

        for (MenuOptionDto eachOption : menuOptions) {
            result.add(save(eachOption));
        }

        return result;
    }

    @Override
    public Map<Long, List<MenuOptionDto>> findAllByMenuIds(Set<Long> menuIds) {
        Map<Long, List<MenuOptionDto>> result = new HashMap<>();

        for (MenuOptionDto eachOption : data.values()) {
            if (menuIds.contains(eachOption.getMenuId())) {
                putItem(eachOption, result);
            }
        }

        return result;
    }

    private void putItem(MenuOptionDto target, Map<Long, List<MenuOptionDto>> source) {
        Long targetMenuId = target.getMenuId();

        if (source.containsKey(targetMenuId)) {
            source.compute(targetMenuId, (key, value)-> {
                value.add(target);
                return value;
            });

        } else {
            List<MenuOptionDto> value = new ArrayList<>();
            value.add(target);

            source.put(targetMenuId, value);
        }
    }
}

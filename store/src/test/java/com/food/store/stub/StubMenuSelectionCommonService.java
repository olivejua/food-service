package com.food.store.stub;

import com.food.common.menu.business.internal.MenuSelectionCommonService;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import com.food.store.mock.MockMenuSelection;

import java.util.*;

public class StubMenuSelectionCommonService implements MenuSelectionCommonService {
    private final Map<Long, MenuSelectionDto> data = new HashMap<>();
    private Long autoIncrementKey = -1L;

    public MenuSelectionDto save(MenuSelectionDto menuSelection) {
        if (data.containsKey(menuSelection.getId())) {
            data.put(menuSelection.getId(), menuSelection);
            return menuSelection;
        }

        MockMenuSelection newOne = MockMenuSelection.testBuilder()
                .id(autoIncrementKey--)
                .optionId(menuSelection.getOptionId())
                .name(menuSelection.getName())
                .amount(menuSelection.getAmount())
                .build();
        data.put(newOne.getId(), newOne);

        return newOne;
    }

    public List<MenuSelectionDto> saveAll(List<MenuSelectionDto> menuOptions) {
        List<MenuSelectionDto> result = new ArrayList<>();

        for (MenuSelectionDto eachSelection : menuOptions) {
            result.add(save(eachSelection));
        }

        return result;
    }

    @Override
    public Map<Long, List<MenuSelectionDto>> findAllByMenuOptionIds(Set<Long> menuOptionIds) {
        Map<Long, List<MenuSelectionDto>> result = new HashMap<>();

        for (MenuSelectionDto eachSelection : data.values()) {
            if (menuOptionIds.contains(eachSelection.getOptionId())) {
                putItem(eachSelection, result);
            }
        }

        return result;
    }

    private void putItem(MenuSelectionDto target, Map<Long, List<MenuSelectionDto>> source) {
        Long targetOptionId = target.getOptionId();

        if (source.containsKey(targetOptionId)) {
            source.compute(targetOptionId, (key, value)-> {
                value.add(target);
                return value;
            });

        } else {
            List<MenuSelectionDto> value = new ArrayList<>();
            value.add(target);

            source.put(targetOptionId, value);
        }
    }
}

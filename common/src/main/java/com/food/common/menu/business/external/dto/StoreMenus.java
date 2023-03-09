package com.food.common.menu.business.external.dto;

import com.food.common.menu.business.internal.dto.MenuDto;
import com.food.common.menu.business.internal.dto.MenuOptionDto;
import com.food.common.menu.business.internal.dto.MenuSelectionDto;
import com.food.common.utils.Amount;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class StoreMenus {
    private final Long storeId;
    private final List<Menu> menus = new ArrayList<>();

    public StoreMenus(Long storeId, List<MenuDto> menus, Map<Long, List<MenuOptionDto>> options, Map<Long, List<MenuSelectionDto>> selections) {
        Assert.notNull(storeId, "storeId must not be null.");
        Assert.notNull(menus, "menus must not be null.");
        Assert.isTrue(!menus.isEmpty(), "menus must not be empty.");

        this.storeId = storeId;
        addAll(menus, options, selections);
    }

    private void addAll(List<MenuDto> menus, Map<Long, List<MenuOptionDto>> options, Map<Long, List<MenuSelectionDto>> selections) {
        for (MenuDto menu : menus) {
            this.menus.add(new Menu(menu, options.get(menu.getId()), selections));
        }
    }

    @Getter
    public static class Menu {
        private Long id;
        private String name;
        private Amount amount;
        private Integer cookingMinutes;
        private final List<Option> options = new ArrayList<>();

        public Menu(MenuDto menu, List<MenuOptionDto> options, Map<Long, List<MenuSelectionDto>> selections) {
            this.id = menu.getId();
            this.name = menu.getName();
            this.amount = menu.getAmount();
            this.cookingMinutes = menu.getCookingMinutes();

            if (!CollectionUtils.isEmpty(options)) {
                addAll(options, selections);
            }
        }

        private void addAll(List<MenuOptionDto> options, Map<Long, List<MenuSelectionDto>> selections) {
            for (MenuOptionDto option : options) {
                this.options.add(new Option(option, selections.get(option.getId())));
            }
        }
    }

    @Getter
    public static class Option {
        private Long id;
        private String name;
        private Byte minSize;
        private Byte maxSize;
        private final List<Selection> selections = new ArrayList<>();

        public Option(MenuOptionDto option, List<MenuSelectionDto> selections) {
            this.id = option.getId();
            this.name = option.getName();
            this.minSize = option.getMinSize();
            this.maxSize = option.getMaxSize();

            if (!CollectionUtils.isEmpty(selections)) {
                addAll(selections);
            }
        }

        private void addAll(List<MenuSelectionDto> selections) {
            if (CollectionUtils.isEmpty(selections)) return;

            List<Selection> addedSelections = selections.stream()
                    .map(Selection::new)
                    .collect(Collectors.toList());
            this.selections.addAll(addedSelections);
        }
    }

    @Getter
    public static class Selection {
        private Long id;
        private String name;
        private Amount amount;

        public Selection(MenuSelectionDto selection) {
            this.id = selection.getId();
            this.name = selection.getName();
            this.amount = selection.getAmount();
        }
    }
}

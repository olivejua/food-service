package com.food.store;

import com.food.common.menu.domain.MenuSelection;
import com.food.common.utils.Amount;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MenuSelectionDto {
    private Long id;
    private Long optionId;
    private String name;
    private Amount amount;

    public MenuSelectionDto(@NotNull MenuSelection entity) {
        this.id = entity.getId();
        this.optionId = entity.getOptionId();
        this.name = entity.getSelection();
        this.amount = Amount.won(entity.getAmount());
    }
}

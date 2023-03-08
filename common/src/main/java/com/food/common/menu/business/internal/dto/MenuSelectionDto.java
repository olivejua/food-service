package com.food.common.menu.business.internal.dto;

import com.food.common.menu.domain.MenuSelection;
import com.food.common.utils.Amount;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MenuSelectionDto {
    protected Long id;
    protected Long optionId;
    protected String name;
    protected Amount amount;

    public MenuSelectionDto(@NotNull MenuSelection entity) {
        this.id = entity.getId();
        this.optionId = entity.getOptionId();
        this.name = entity.getName();
        this.amount = Amount.won(entity.getAmount());
    }
}

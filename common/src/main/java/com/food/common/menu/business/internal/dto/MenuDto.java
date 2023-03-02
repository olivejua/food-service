package com.food.common.menu.business.internal.dto;

import com.food.common.menu.domain.Menu;
import com.food.common.utils.Amount;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class MenuDto {
    protected Long id;
    protected Long storeId;
    protected String name;
    protected Amount amount;
    protected Integer cookingMinutes;

    public MenuDto(@NotNull final Menu entity) {
        this.id = entity.getId();
        this.storeId = entity.getStoreId();
        this.name = entity.getName();
        this.amount = Amount.won(entity.getAmount());
        this.cookingMinutes = entity.getCookingMinutes();
    }
}

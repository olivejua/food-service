package com.food.common.store.business.internal.dto;

import com.food.common.store.domain.Store;
import com.food.common.store.domain.type.OpenStatus;
import com.food.common.utils.Amount;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class StoreDto {
    protected Long id;
    protected String name;
    protected Amount minOrderAmount;
    protected OpenStatus status;

    public StoreDto(@NotNull Store entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.minOrderAmount = Amount.won(entity.getMinOrderAmount());
        this.status = entity.getStatus();
    }

    public boolean isClosed() {
        return status == OpenStatus.CLOSED;
    }
}

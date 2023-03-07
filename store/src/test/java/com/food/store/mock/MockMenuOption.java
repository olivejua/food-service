package com.food.store.mock;

import com.food.common.menu.business.internal.dto.MenuOptionDto;
import lombok.Builder;

public class MockMenuOption extends MenuOptionDto {

    @Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
    public MockMenuOption(Long id, Long menuId, String name, Byte minSize, Byte maxSize) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }
}

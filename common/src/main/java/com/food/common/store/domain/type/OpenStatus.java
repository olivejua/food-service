package com.food.common.store.domain.type;

public enum OpenStatus {
    OPEN("운영 중"),
    CLOSED("운영 종료")
    ;

    private final String description;

    OpenStatus(String description) {
        this.description = description;
    }
}

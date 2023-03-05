package com.food.store.error;

import com.food.common.error.ApplicationErrors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MenuErrors implements ApplicationErrors {
    NOT_FOUND_STORE_TO_FIND_MENUS("MENU_0001", "메뉴 조회할 가게 정보를 찾을 수 없습니다."),
    ;

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

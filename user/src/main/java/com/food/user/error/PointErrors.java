package com.food.user.error;

import com.food.common.error.ApplicationErrors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointErrors implements ApplicationErrors {
    NOT_FOUND_OWNER("POINT_0001", "포인트 소유자를 찾을 수 없습니다."),
    NOT_FOUND_PAYMENT("POINT_0002", "포인트를 적립할 결제정보를 찾을 수 없습니다."),
    INVALID_USED_POINTS_UNIT("POINT_0003", "사용 포인트는 0 이상이어야 하며, 10원 단위여야 합니다."),
    INSUFFICIENT_POINT_BALANCE("POINT_0004", "포인트 잔액이 부족합니다."),
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

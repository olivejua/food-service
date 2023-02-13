package com.food.order.mock;

import com.food.common.user.business.external.model.RequestUser;
import com.food.common.user.enumeration.AccountType;

public class MockRequestUser implements RequestUser {
    private Long userId;

    public MockRequestUser(Long userId) {
        this.userId = userId;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public Long getAccountId() {
        return null;
    }

    @Override
    public AccountType getAccountType() {
        return null;
    }
}
